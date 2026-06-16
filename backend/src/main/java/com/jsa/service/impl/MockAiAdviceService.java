package com.jsa.service.impl;

import com.jsa.dto.request.AiAdviceRequest;
import com.jsa.dto.response.AiAdviceResponse;
import com.jsa.service.AiAdviceService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AI 健身建议 —— 一期 Mock 实现（见 docs/05 第4、6节，里程碑一）。
 * 返回模板建议，使主流程不依赖外部大模型即可端到端演示。
 *
 * 仅当 ai.enabled != true 时启用（缺省即启用）；
 * 二期实现 RealAiAdviceService 并设 ai.enabled=true 切换。
 */
@Service
@ConditionalOnProperty(name = "ai.enabled", havingValue = "false", matchIfMissing = true)
public class MockAiAdviceService implements AiAdviceService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public AiAdviceResponse advise(Long userId, AiAdviceRequest request) {
        String answer = "【示例建议】关于「" + request.getQuestion() + "」：建议保证规律作息与充足蛋白质摄入，"
                + "循序渐进增加训练量，有氧与力量结合，注意热身与拉伸。（此为 Mock 数据，接入真实 AI 后替换）";
        return new AiAdviceResponse(answer, "mock", LocalDateTime.now().format(FMT));
    }
}
