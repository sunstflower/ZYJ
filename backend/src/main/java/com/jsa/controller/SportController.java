package com.jsa.controller;

import com.jsa.common.Result;
import com.jsa.dto.response.SportVO;
import com.jsa.service.SportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 运动项目接口（见 docs/04 3.2）。需登录。
 */
@RestController
@RequestMapping("/api/sports")
public class SportController {

    private final SportService sportService;

    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    /** GET /api/sports */
    @GetMapping
    public Result<List<SportVO>> list() {
        return Result.success(sportService.listAll());
    }
}
