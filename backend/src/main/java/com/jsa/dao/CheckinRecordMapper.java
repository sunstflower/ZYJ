package com.jsa.dao;

import com.jsa.dto.response.CheckinVO;
import com.jsa.entity.CheckinRecord;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 打卡记录 DAO（MyBatis Mapper）。
 * SQL 见 resources/mapper/CheckinRecordMapper.xml。
 * 列表查询返回联表视图 CheckinVO（关联 user / sport）。
 */
public interface CheckinRecordMapper {

    /** 新增打卡记录，回填自增主键 */
    int insert(CheckinRecord record);

    /** 按 id 查询原始记录（审核时校验状态用） */
    CheckinRecord findById(@Param("id") Long id);

    /** 查询某用户的打卡记录（联表，含运动项目名），可按状态过滤 */
    List<CheckinVO> findByUser(@Param("userId") Long userId, @Param("status") String status);

    /** 查询全部打卡记录（联表，含用户名/项目名/审核人），可按状态过滤（管理员） */
    List<CheckinVO> findAll(@Param("status") String status);

    /** 更新审核结果：状态、审核人、审核时间 */
    int updateReview(@Param("id") Long id,
                     @Param("status") String status,
                     @Param("reviewerId") Long reviewerId,
                     @Param("reviewTime") LocalDateTime reviewTime);
}
