package com.jsa.service.impl;

import com.jsa.common.TokenStore;
import com.jsa.dao.UserMapper;
import com.jsa.dto.request.LoginRequest;
import com.jsa.dto.response.LoginResponse;
import com.jsa.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * 认证业务实现。
 *
 * TODO（业务实现阶段）：
 *  1. 按 username 查 user；
 *  2. 校验密码（演示可明文比对，后续可换 BCrypt）；
 *  3. 失败抛 BusinessException(ResultCode.LOGIN_FAILED)；
 *  4. 成功用 TokenStore 下发 token，组装 LoginResponse（含 UserVO）。
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
        // TODO: 实现登录校验与 token 下发
        throw new UnsupportedOperationException("AuthService.login 尚未实现（骨架阶段）");
    }
}
