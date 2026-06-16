package com.jsa.service.impl;

import com.jsa.dao.SportMapper;
import com.jsa.dto.response.SportVO;
import com.jsa.entity.Sport;
import com.jsa.service.SportService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 运动项目业务实现（见 docs/04 3.2）。
 */
@Service
public class SportServiceImpl implements SportService {

    private final SportMapper sportMapper;

    public SportServiceImpl(SportMapper sportMapper) {
        this.sportMapper = sportMapper;
    }

    @Override
    public List<SportVO> listAll() {
        return sportMapper.findAll().stream()
                .map(this::toVO)
                .toList();
    }

    private SportVO toVO(Sport s) {
        return new SportVO(s.getId(), s.getName(), s.getDescription());
    }
}
