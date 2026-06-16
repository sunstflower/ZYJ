package com.jsa.entity;

import java.time.LocalDateTime;

/**
 * 打卡记录实体，对应表 checkin_record（见 docs/03 3.3）。
 * 通过 userId / sportId 外键关联 user 与 sport；reviewerId 指向审核管理员。
 * status：PENDING / APPROVED / REJECTED。
 */
public class CheckinRecord {

    private Long id;
    private Long userId;
    private Long sportId;
    private String content;
    private String status;
    private LocalDateTime checkinTime;
    private Long reviewerId;
    private LocalDateTime reviewTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(LocalDateTime checkinTime) {
        this.checkinTime = checkinTime;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public LocalDateTime getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(LocalDateTime reviewTime) {
        this.reviewTime = reviewTime;
    }
}
