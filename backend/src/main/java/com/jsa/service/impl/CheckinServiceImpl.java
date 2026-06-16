package com.jsa.service.impl;

import com.jsa.common.BusinessException;
import com.jsa.common.CheckinStatus;
import com.jsa.common.ResultCode;
import com.jsa.dao.CheckinRecordMapper;
import com.jsa.dao.SportMapper;
import com.jsa.dto.request.CheckinCreateRequest;
import com.jsa.dto.request.ReviewRequest;
import com.jsa.dto.response.CheckinVO;
import com.jsa.entity.CheckinRecord;
import com.jsa.entity.Sport;
import com.jsa.service.CheckinService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 打卡业务实现（见 docs/04 3.3 ~ 3.6）。
 * 打卡时间、审核时间、默认状态均由服务端生成，前端不可篡改（见 docs/01 业务规则）。
 */
@Service
public class CheckinServiceImpl implements CheckinService {

    private final CheckinRecordMapper checkinRecordMapper;
    private final SportMapper sportMapper;

    public CheckinServiceImpl(CheckinRecordMapper checkinRecordMapper, SportMapper sportMapper) {
        this.checkinRecordMapper = checkinRecordMapper;
        this.sportMapper = sportMapper;
    }

    @Override
    @Transactional
    public CheckinVO submit(Long userId, CheckinCreateRequest request) {
        // 校验运动项目存在（保证外键有效，见 docs/01 业务规则 6）
        Sport sport = sportMapper.findById(request.getSportId());
        if (sport == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "运动项目不存在");
        }

        CheckinRecord record = new CheckinRecord();
        record.setUserId(userId);
        record.setSportId(request.getSportId());
        record.setContent(request.getContent());
        record.setStatus(CheckinStatus.PENDING.name());     // 默认待审核
        record.setCheckinTime(LocalDateTime.now());          // 服务端时间
        checkinRecordMapper.insert(record);                  // 回填自增 id

        // 组装返回视图（docs/04 3.3）
        CheckinVO vo = new CheckinVO();
        vo.setId(record.getId());
        vo.setSportId(sport.getId());
        vo.setSportName(sport.getName());
        vo.setContent(record.getContent());
        vo.setStatus(record.getStatus());
        vo.setCheckinTime(record.getCheckinTime());
        return vo;
    }

    @Override
    public List<CheckinVO> listMine(Long userId, String status) {
        return checkinRecordMapper.findByUser(userId, status);
    }

    @Override
    public List<CheckinVO> listAll(String status) {
        return checkinRecordMapper.findAll(status);
    }

    @Override
    @Transactional
    public CheckinVO review(Long recordId, Long reviewerId, ReviewRequest request) {
        CheckinRecord record = checkinRecordMapper.findById(recordId);
        if (record == null) {
            throw new BusinessException(ResultCode.RECORD_NOT_FOUND);
        }
        // 仅待审核记录可审核（docs/01 业务规则 3）
        if (!CheckinStatus.PENDING.name().equals(record.getStatus())) {
            throw new BusinessException(ResultCode.RECORD_NOT_PENDING);
        }

        CheckinStatus target = mapAction(request.getAction());
        LocalDateTime reviewTime = LocalDateTime.now();
        checkinRecordMapper.updateReview(recordId, target.name(), reviewerId, reviewTime);

        // 返回审核结果（docs/04 3.6）
        CheckinVO vo = new CheckinVO();
        vo.setId(recordId);
        vo.setStatus(target.name());
        vo.setReviewerId(reviewerId);
        vo.setReviewTime(reviewTime);
        return vo;
    }

    /** action(APPROVE/REJECT) -> 目标状态(APPROVED/REJECTED) */
    private CheckinStatus mapAction(String action) {
        if ("APPROVE".equalsIgnoreCase(action)) {
            return CheckinStatus.APPROVED;
        }
        if ("REJECT".equalsIgnoreCase(action)) {
            return CheckinStatus.REJECTED;
        }
        throw new BusinessException(ResultCode.BAD_REQUEST, "无效的审核动作，应为 APPROVE 或 REJECT");
    }
}
