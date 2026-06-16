package com.jsa.service;

import com.jsa.dto.request.LoginRequest;
import com.jsa.dto.response.LoginResponse;

/**
 * 认证业务（见 docs/04 3.1）。
 */
public interface AuthService {

    /** 登录校验，成功下发 token 与用户信息；失败抛 BusinessException(LOGIN_FAILED) */
    LoginResponse login(LoginRequest request);
}
