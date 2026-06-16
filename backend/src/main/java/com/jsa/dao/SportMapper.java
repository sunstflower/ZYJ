package com.jsa.dao;

import com.jsa.entity.Sport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运动项目 DAO（MyBatis Mapper）。
 * SQL 见 resources/mapper/SportMapper.xml。
 */
public interface SportMapper {

    /** 查询全部运动项目 */
    List<Sport> findAll();

    /** 按 id 查询（校验项目是否存在） */
    Sport findById(@Param("id") Long id);
}
