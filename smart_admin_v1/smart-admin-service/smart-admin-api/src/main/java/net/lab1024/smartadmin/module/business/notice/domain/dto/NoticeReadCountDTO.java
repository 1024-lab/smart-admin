package net.lab1024.smartadmin.module.business.notice.domain.dto;

import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/7/12 0012 上午 8:11
 * @since JDK1.8
 */
@Data
public class NoticeReadCountDTO {
    /**
     * 员工id
     */
    private Long employeeId;
    /**
     * 已读消息数
     */
    private Integer readCount;

}
