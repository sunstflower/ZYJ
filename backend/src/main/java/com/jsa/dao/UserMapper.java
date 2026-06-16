package com.jsa.dao;

import com.jsa.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户 DAO（MyBatis Mapper，见 docs/02 分层）。
 * SQL 见 resources/mapper/UserMapper.xml。
 */
public interface UserMapper {

    /** 按用户名查询（登录用） */
    User findByUsername(@Param("username") String username);

    /** 按 id 查询 */
    User findById(@Param("id") Long id);
}
