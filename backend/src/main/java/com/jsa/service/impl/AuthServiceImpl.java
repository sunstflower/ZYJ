package com.jsa.service.impl;

import com.jsa.common.AuthSession;
import com.jsa.common.BusinessException;
import com.jsa.common.ResultCode;
import com.jsa.common.TokenStore;
import com.jsa.dao.UserMapper;
import com.jsa.dto.request.LoginRequest;
import com.jsa.dto.request.RegisterRequest;
import com.jsa.dto.response.LoginResponse;
import com.jsa.dto.response.UserVO;
import com.jsa.entity.User;
import com.jsa.service.AuthService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 认证业务实现（见 docs/04 3.1 / 3.7）。
 * 演示阶段密码明文存储与比对；如需提升可改为 BCrypt（需同步改登录与种子数据，见 docs/03 备注）。
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
        return issueFor(user);
    }

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        // 先查重，给出友好提示（DB 唯一键为并发兜底）
        if (userMapper.findByUsername(request.getUsername()) != null) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());      // 演示明文，与登录比对一致
        user.setNickname(request.getNickname());
        user.setRole("USER");                          // 仅开放普通用户注册，管理员不可自助注册
        user.setCreateTime(LocalDateTime.now());
        try {
            userMapper.insert(user);
        } catch (DataIntegrityViolationException e) {
            // 并发下唯一键冲突兜底，转为友好业务错误
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }
        // 注册即登录：直接签发 token
        return issueFor(user);
    }

    /** 为已校验用户签发 token 并组装登录响应（login / register 共用） */
    private LoginResponse issueFor(User user) {
        String token = tokenStore.issue(
                new AuthSession(user.getId(), user.getUsername(), user.getRole()));
        UserVO userVO = new UserVO(user.getId(), user.getUsername(), user.getNickname(), user.getRole());
        return new LoginResponse(token, userVO);
    }
}
