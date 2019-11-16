package com.gangquan360.smartadmin.module.notice.domain.entity;
import com.baomidou.mybatisplus.annotations.TableName;
import com.gangquan360.smartadmin.common.domain.BaseEntity;
import lombok.Data;

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
@Data
@TableName("t_notice_receive_record")
public class NoticeReceiveRecordEntity extends BaseEntity{


    /**
     * 消息id
     */
    private Long noticeId;

    /**
     * 消息接收人
     */
    private Long employeeId;



}
