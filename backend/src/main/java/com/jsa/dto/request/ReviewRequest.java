package com.jsa.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 审核请求体（见 docs/04 3.6）。
 * action：APPROVE（通过）/ REJECT（驳回）。
 */
public class ReviewRequest {

    @NotBlank(message = "审核动作不能为空")
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
