package com.jsa.controller;

import com.jsa.common.AuthSession;
import com.jsa.common.BusinessException;
import com.jsa.common.Result;
import com.jsa.common.ResultCode;
import com.jsa.dto.request.CheckinCreateRequest;
import com.jsa.dto.request.ReviewRequest;
import com.jsa.dto.response.CheckinVO;
import com.jsa.interceptor.AuthInterceptor;
import com.jsa.service.CheckinService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 打卡接口（见 docs/04 3.3 ~ 3.6）。
 * 登录态由拦截器校验；管理员权限在本控制器内校验。
 */
@RestController
@RequestMapping("/api/checkins")
public class CheckinController {

    private final CheckinService checkinService;

    public CheckinController(CheckinService checkinService) {
        this.checkinService = checkinService;
    }

    /** POST /api/checkins —— 提交打卡（普通用户） */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result<CheckinVO> submit(@Valid @RequestBody CheckinCreateRequest request,
                                    HttpServletRequest http) {
        AuthSession session = AuthInterceptor.currentSession(http);
        return Result.success("打卡成功，等待审核",
                checkinService.submit(session.userId(), request));
    }

    /** GET /api/checkins/mine —— 查看本人打卡记录 */
    @GetMapping("/mine")
    public Result<List<CheckinVO>> listMine(@RequestParam(required = false) String status,
                                            HttpServletRequest http) {
        AuthSession session = AuthInterceptor.currentSession(http);
        return Result.success(checkinService.listMine(session.userId(), status));
    }

    /** GET /api/checkins —— 查看全部打卡记录（管理员） */
    @GetMapping
    public Result<List<CheckinVO>> listAll(@RequestParam(required = false) String status,
                                           HttpServletRequest http) {
        requireAdmin(http);
        return Result.success(checkinService.listAll(status));
    }

    /** PATCH /api/checkins/{id}/review —— 审核打卡（管理员） */
    @PatchMapping("/{id}/review")
    public Result<CheckinVO> review(@PathVariable Long id,
                                    @Valid @RequestBody ReviewRequest request,
                                    HttpServletRequest http) {
        AuthSession session = requireAdmin(http);
        return Result.success("审核完成",
                checkinService.review(id, session.userId(), request));
    }

    /** 校验当前会话是否为管理员，否则抛 403 */
    private AuthSession requireAdmin(HttpServletRequest http) {
        AuthSession session = AuthInterceptor.currentSession(http);
        if (session == null || !session.isAdmin()) {
            throw new BusinessException(ResultCode.FORBIDDEN, "需要管理员权限");
        }
        return session;
    }
}
