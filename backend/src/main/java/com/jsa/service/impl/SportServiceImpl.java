package com.jsa.service.impl;

import com.jsa.dao.SportMapper;
import com.jsa.dto.response.SportVO;
import com.jsa.service.SportService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 运动项目业务实现。
 *
 * TODO（业务实现阶段）：查询全部运动项目并转换为 SportVO。
 */
@Service
public class SportServiceImpl implements SportService {

    private final SportMapper sportMapper;

    public SportServiceImpl(SportMapper sportMapper) {
        this.sportMapper = sportMapper;
    }

    @Override
    public List<SportVO> listAll() {
        // TODO: sportMapper.findAll() -> List<SportVO>
        throw new UnsupportedOperationException("SportService.listAll 尚未实现（骨架阶段）");
    }
}
