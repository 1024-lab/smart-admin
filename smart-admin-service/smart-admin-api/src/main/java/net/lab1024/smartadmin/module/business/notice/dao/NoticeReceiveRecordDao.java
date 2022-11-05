package net.lab1024.smartadmin.module.business.notice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.smartadmin.module.business.notice.domain.entity.NoticeReceiveRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019-07-11 16:19:48
 * @since JDK1.8
 */
@Mapper
@Component
public interface NoticeReceiveRecordDao extends BaseMapper<NoticeReceiveRecordEntity> {

    /**
     * 批量删除
     *
     * @param noticeId
     * @return
     */
    void deleteByNoticeId(@Param("noticeId") Long noticeId);

    /**
     * 批量插入
     *
     * @param rolePrivilegeList
     */
    void batchInsert(List<NoticeReceiveRecordEntity> rolePrivilegeList);

    /**
     * 根据员工和系统通知获取读取记录
     *
     * @param employeeId
     * @param noticeId
     * @return
     */
    NoticeReceiveRecordEntity selectByEmployeeAndNotice(@Param("employeeId") Long employeeId, @Param("noticeId") Long noticeId);
}
