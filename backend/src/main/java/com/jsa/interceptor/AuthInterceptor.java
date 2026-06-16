package com.jsa.interceptor;

import com.jsa.common.AuthSession;
import com.jsa.common.BusinessException;
import com.jsa.common.ResultCode;
import com.jsa.common.TokenStore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器（见 docs/02 第6节）。
 * 校验请求头中的 token，解析出登录会话并放入请求属性，供 Controller 读取。
 * 管理员权限（role==ADMIN）的细粒度校验在对应 Controller/Service 中进行。
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /** 请求属性键：当前登录会话 */
    public static final String ATTR_SESSION = "authSession";

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final TokenStore tokenStore;

    public AuthInterceptor(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = extractToken(request);
        AuthSession session = tokenStore.resolve(token);
        if (session == null) {
            // 由全局异常处理器统一转为 401
            throw new BusinessException(ResultCode.UNAUTHORIZED, "未登录或登录已失效，请重新登录");
        }
        request.setAttribute(ATTR_SESSION, session);
        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER);
        if (header == null) {
            return null;
        }
        return header.startsWith(PREFIX) ? header.substring(PREFIX.length()) : header;
    }

    /** 供 Controller 获取当前登录会话 */
    public static AuthSession currentSession(HttpServletRequest request) {
        return (AuthSession) request.getAttribute(ATTR_SESSION);
    }
}
