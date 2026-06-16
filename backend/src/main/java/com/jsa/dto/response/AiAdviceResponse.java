package com.jsa.dto.response;

/**
 * AI 健身建议响应（预留，见 docs/05 3.1）。
 */
public class AiAdviceResponse {

    private String answer;
    private String model;
    private String createdAt;

    public AiAdviceResponse() {
    }

    public AiAdviceResponse(String answer, String model, String createdAt) {
        this.answer = answer;
        this.model = model;
        this.createdAt = createdAt;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
