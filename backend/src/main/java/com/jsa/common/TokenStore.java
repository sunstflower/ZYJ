package com.jsa.common;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 演示级 token 存储（内存实现，见 docs/02 第6节、决策 D-4）。
 * 重启失效；如需持久/无状态，后续可替换为 JWT。
 */
@Component
public class TokenStore {

    private final ConcurrentHashMap<String, AuthSession> sessions = new ConcurrentHashMap<>();

    /** 登录成功后下发 token */
    public String issue(AuthSession session) {
        String token = UUID.randomUUID().toString().replace("-", "");
        sessions.put(token, session);
        return token;
    }

    /** 由 token 取会话；不存在返回 null */
    public AuthSession resolve(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        return sessions.get(token);
    }

    /** 登出/失效 */
    public void revoke(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }
}
