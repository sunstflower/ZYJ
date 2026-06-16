package com.jsa.service.impl;

import com.jsa.common.AuthSession;
import com.jsa.common.BusinessException;
import com.jsa.common.ResultCode;
import com.jsa.common.TokenStore;
import com.jsa.dao.UserMapper;
import com.jsa.dto.request.LoginRequest;
import com.jsa.dto.response.LoginResponse;
import com.jsa.dto.response.UserVO;
import com.jsa.entity.User;
import com.jsa.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * 认证业务实现（见 docs/04 3.1）。
 * 演示阶段密码明文比对；如需提升可改为 BCrypt（见 docs/03 备注）。
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final TokenStore tokenStore;

    public AuthServiceImpl(UserMapper userMapper, TokenStore tokenStore) {
        this.userMapper = userMapper;
        this.tokenStore = tokenStore;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        // 用户不存在或密码不匹配，统一返回"用户名或密码错误"（不泄露具体哪一项）
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            throw new BusinessException(ResultCode.LOGIN_FAILED);
        }

        String token = tokenStore.issue(
                new AuthSession(user.getId(), user.getUsername(), user.getRole()));

        UserVO userVO = new UserVO(user.getId(), user.getUsername(), user.getNickname(), user.getRole());
        return new LoginResponse(token, userVO);
    }
}
