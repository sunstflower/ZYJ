package com.jsa.controller;

import com.jsa.common.AuthSession;
import com.jsa.common.Result;
import com.jsa.dto.request.AiAdviceRequest;
import com.jsa.dto.response.AiAdviceResponse;
import com.jsa.interceptor.AuthInterceptor;
import com.jsa.service.AiAdviceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI 健身建议接口（预留，见 docs/05 3.1）。需登录。
 * 一期由 MockAiAdviceService 提供假数据。
 */
@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiAdviceService aiAdviceService;

    public AiController(AiAdviceService aiAdviceService) {
        this.aiAdviceService = aiAdviceService;
    }

    /** POST /api/ai/advice */
    @PostMapping("/advice")
    public Result<AiAdviceResponse> advice(@Valid @RequestBody AiAdviceRequest request,
                                           HttpServletRequest http) {
        AuthSession session = AuthInterceptor.currentSession(http);
        return Result.success(aiAdviceService.advise(session.userId(), request));
    }
}
