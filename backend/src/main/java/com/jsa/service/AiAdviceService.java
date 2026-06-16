package com.jsa.service;

import com.jsa.dto.request.AiAdviceRequest;
import com.jsa.dto.response.AiAdviceResponse;

/**
 * AI 健身建议业务（预留，见 docs/05）。
 * 一期由 MockAiAdviceService 返回假数据；二期实现 RealAiAdviceService 接入大模型。
 */
public interface AiAdviceService {

    AiAdviceResponse advise(Long userId, AiAdviceRequest request);
}
