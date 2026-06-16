package com.jsa.service.impl;

import com.jsa.dao.CheckinRecordMapper;
import com.jsa.dao.SportMapper;
import com.jsa.dto.request.CheckinCreateRequest;
import com.jsa.dto.request.ReviewRequest;
import com.jsa.dto.response.CheckinVO;
import com.jsa.service.CheckinService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 打卡业务实现。
 *
 * TODO（业务实现阶段）：
 *  - submit：校验运动项目存在；构造 CheckinRecord（status=PENDING，checkinTime=now）；插入；返回 VO。
 *  - listMine：按 userId(+status) 查询本人记录。
 *  - listAll：按 status 查询全部记录（管理员）。
 *  - review：校验记录存在且为 PENDING；按 action 映射 APPROVED/REJECTED；
 *            写入 reviewerId、reviewTime；非 PENDING 抛 RECORD_NOT_PENDING。
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
    public CheckinVO submit(Long userId, CheckinCreateRequest request) {
        // TODO: 提交打卡
        throw new UnsupportedOperationException("CheckinService.submit 尚未实现（骨架阶段）");
    }

    @Override
    public List<CheckinVO> listMine(Long userId, String status) {
        // TODO: 查询本人打卡记录
        throw new UnsupportedOperationException("CheckinService.listMine 尚未实现（骨架阶段）");
    }

    @Override
    public List<CheckinVO> listAll(String status) {
        // TODO: 查询全部打卡记录
        throw new UnsupportedOperationException("CheckinService.listAll 尚未实现（骨架阶段）");
    }

    @Override
    public CheckinVO review(Long recordId, Long reviewerId, ReviewRequest request) {
        // TODO: 审核打卡
        throw new UnsupportedOperationException("CheckinService.review 尚未实现（骨架阶段）");
    }
}
