package com.jsa.service;

import com.jsa.dto.response.SportVO;

import java.util.List;

/**
 * 运动项目业务（见 docs/04 3.2）。
 */
public interface SportService {

    /** 获取全部运动项目 */
    List<SportVO> listAll();
}
