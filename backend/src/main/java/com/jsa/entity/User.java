package com.jsa.entity;

import java.time.LocalDateTime;

/**
 * 用户实体，对应表 user（见 docs/03 3.1）。
 * 管理员与普通用户复用同一张表，用 role 区分（USER / ADMIN）。
 */
public class User {

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String role;
    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
