package com.jsa.common;

/**
 * 打卡审核状态（见 docs/03 第4节）。
 */
public enum CheckinStatus {
    PENDING,   // 待审核（默认）
    APPROVED,  // 审核通过
    REJECTED   // 审核驳回
}
