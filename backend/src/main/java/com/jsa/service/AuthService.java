package com.jsa.service;

import com.jsa.dto.request.LoginRequest;
import com.jsa.dto.request.RegisterRequest;
import com.jsa.dto.response.LoginResponse;

/**
 * 认证业务（见 docs/04 3.1 / 3.7）。
 */
public interface AuthService {

    /** 登录校验，成功下发 token 与用户信息；失败抛 BusinessException(LOGIN_FAILED) */
    LoginResponse login(LoginRequest request);

    /**
     * 注册普通用户（角色强制 USER），成功后直接签发 token（注册即登录）。
     * 用户名重复抛 BusinessException(USERNAME_EXISTS)。
     */
    LoginResponse register(RegisterRequest request);
}
