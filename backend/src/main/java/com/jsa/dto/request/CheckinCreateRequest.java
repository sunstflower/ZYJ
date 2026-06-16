package com.jsa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 提交打卡请求体（见 docs/04 3.3）。
 * userId / checkinTime / status 由服务端补充，前端不传。
 */
public class CheckinCreateRequest {

    @NotNull(message = "运动项目不能为空")
    private Long sportId;

    @NotBlank(message = "打卡内容不能为空")
    @Size(max = 500, message = "打卡内容不能超过500字")
    private String content;

    public Long getSportId() {
        return sportId;
    }

    public void setSportId(Long sportId) {
        this.sportId = sportId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
