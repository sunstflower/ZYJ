package com.jsa.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * AI 健身建议请求体（预留，见 docs/05 3.1）。
 */
public class AiAdviceRequest {

    @NotBlank(message = "问题不能为空")
    private String question;

    /** 是否结合本人近期打卡记录作为上下文，默认 false */
    private boolean includeHistory = false;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isIncludeHistory() {
        return includeHistory;
    }

    public void setIncludeHistory(boolean includeHistory) {
        this.includeHistory = includeHistory;
    }
}
