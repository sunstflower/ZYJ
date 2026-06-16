package com.jsa.service.impl;

import com.jsa.common.CheckinStatus;
import com.jsa.dao.CheckinRecordMapper;
import com.jsa.dto.request.AiAdviceRequest;
import com.jsa.dto.response.AiAdviceResponse;
import com.jsa.dto.response.CheckinVO;
import com.jsa.service.AiAdviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * AI 健身建议 —— 二期真实实现（见 docs/05 第4/6节，里程碑二/三）。
 * 通过后端代理调用 DeepSeek（OpenAI 兼容 /chat/completions），Key 仅存后端，绝不下发前端。
 *
 * 仅当 ai.enabled=true 时启用；缺省走 {@link MockAiAdviceService}。
 * 失败/超时按 docs/05 §3 降级：返回 model="fallback" 的通用建议，不抛 500、不影响其它功能。
 * includeHistory=true 时拼接本人近期「已通过」打卡作为上下文（docs/05 §5，仅本人数据）。
 */
@Service
@ConditionalOnProperty(name = "ai.enabled", havingValue = "true")
public class RealAiAdviceService implements AiAdviceService {

    private static final Logger log = LoggerFactory.getLogger(RealAiAdviceService.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int HISTORY_LIMIT = 10;
    private static final String SYSTEM_PROMPT =
            "你是一名专业、严谨的健身教练助手。请根据用户情况给出科学、可执行的健身与营养建议，"
            + "语言简洁友好，避免医疗诊断。如涉及伤病请提醒用户咨询专业医生。";

    private final CheckinRecordMapper checkinRecordMapper;
    private final RestClient client;
    private final String apiKey;
    private final String model;

    public RealAiAdviceService(CheckinRecordMapper checkinRecordMapper,
                               @Value("${ai.base-url:https://api.deepseek.com}") String baseUrl,
                               @Value("${ai.api-key:}") String apiKey,
                               @Value("${ai.model:deepseek-chat}") String model,
                               @Value("${ai.timeout-ms:30000}") long timeoutMs) {
        this.checkinRecordMapper = checkinRecordMapper;
        this.apiKey = apiKey;
        this.model = model;
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(10))
                .withReadTimeout(Duration.ofMillis(timeoutMs));
        this.client = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(ClientHttpRequestFactories.get(settings))
                .build();
        log.info("RealAiAdviceService 启用：baseUrl={}, model={}, apiKey={}", baseUrl, model,
                apiKey == null || apiKey.isBlank() ? "<空, 将走降级>" : "<已配置>");
    }

    @Override
    public AiAdviceResponse advise(Long userId, AiAdviceRequest request) {
        String now = LocalDateTime.now().format(FMT);
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("ai.api-key 未配置，返回降级建议");
            return fallback(now);
        }
        try {
            String userContent = buildUserContent(userId, request);
            ChatReq body = new ChatReq(model, List.of(
                    new Msg("system", SYSTEM_PROMPT),
                    new Msg("user", userContent)), false);

            ChatResp resp = client.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(ChatResp.class);

            String answer = resp == null ? null
                    : resp.choices() == null || resp.choices().isEmpty() ? null
                    : resp.choices().get(0).message() == null ? null
                    : resp.choices().get(0).message().content();
            if (answer == null || answer.isBlank()) {
                log.warn("AI 返回空内容，降级。resp={}", resp);
                return fallback(now);
            }
            String usedModel = resp.model() == null ? model : resp.model();
            return new AiAdviceResponse(answer.trim(), usedModel, now);
        } catch (Exception e) {
            // docs/05 §3：可降级。外部服务异常不外抛，返回友好提示
            log.error("调用 AI 失败，返回降级建议：{}", e.getMessage(), e);
            return fallback(now);
        }
    }

    /** 组装发给模型的用户消息：可选拼接本人近期已通过打卡作为上下文（docs/05 §5） */
    private String buildUserContent(Long userId, AiAdviceRequest request) {
        String question = request.getQuestion();
        if (!request.isIncludeHistory()) {
            return question;
        }
        List<CheckinVO> history;
        try {
            history = checkinRecordMapper.findByUser(userId, CheckinStatus.APPROVED.name());
        } catch (Exception e) {
            log.warn("读取打卡历史失败，忽略历史上下文：{}", e.getMessage());
            return question;
        }
        if (history == null || history.isEmpty()) {
            return question;
        }
        StringBuilder sb = new StringBuilder("用户近期已通过的健身记录：\n");
        int n = 0;
        for (CheckinVO r : history) {
            if (n++ >= HISTORY_LIMIT) {
                break;
            }
            String day = r.getCheckinTime() == null ? "" : r.getCheckinTime().format(DAY);
            sb.append("- ").append(day).append(' ')
              .append(r.getSportName() == null ? "" : r.getSportName())
              .append("：").append(r.getContent() == null ? "" : r.getContent())
              .append('\n');
        }
        sb.append("请结合以上记录，回答用户的问题：").append(question);
        return sb.toString();
    }

    private AiAdviceResponse fallback(String now) {
        return new AiAdviceResponse(
                "AI 服务暂不可用，以下为通用建议：保证规律作息与充足睡眠，每日摄入足量蛋白质，"
                + "训练循序渐进、有氧与力量结合，注意热身与拉伸，避免过度训练。",
                "fallback", now);
    }

    // —— DeepSeek（OpenAI 兼容）请求/响应最小映射；未知字段由 Jackson 默认忽略 ——
    private record ChatReq(String model, List<Msg> messages, boolean stream) {
    }

    private record Msg(String role, String content) {
    }

    private record ChatResp(String model, List<Choice> choices) {
    }

    private record Choice(Msg message) {
    }
}
