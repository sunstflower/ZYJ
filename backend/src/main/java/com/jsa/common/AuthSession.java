package com.jsa.common;

/**
 * 登录会话信息（演示级，见 docs/02 第6节、决策 D-4）。
 * 由 token 映射得到，携带当前登录用户的 id 与角色。
 */
public record AuthSession(Long userId, String username, String role) {

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}
