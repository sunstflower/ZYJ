package com.jsa.service;

import com.jsa.dto.request.CheckinCreateRequest;
import com.jsa.dto.request.ReviewRequest;
import com.jsa.dto.response.CheckinVO;

import java.util.List;

/**
 * 打卡业务（见 docs/04 3.3 ~ 3.6）。
 */
public interface CheckinService {

    /** 提交打卡（普通用户）。服务端补充用户、时间、默认状态 PENDING */
    CheckinVO submit(Long userId, CheckinCreateRequest request);

    /** 查看本人打卡记录（普通用户），可按状态过滤 */
    List<CheckinVO> listMine(Long userId, String status);

    /** 查看全部打卡记录（管理员），可按状态过滤 */
    List<CheckinVO> listAll(String status);

    /** 审核打卡（管理员）：APPROVE / REJECT */
    CheckinVO review(Long recordId, Long reviewerId, ReviewRequest request);
}
