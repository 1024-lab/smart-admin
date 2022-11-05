package net.lab1024.smartadmin.module.business.notice.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/7/12 0012 下午 12:32
 * @since JDK1.8
 */
@Data
public class NoticeReceiveQueryDTO extends NoticeQueryDTO{

    @ApiModelProperty(value = "当前登录人",hidden = true)
    private Long employeeId;

    @ApiModelProperty(value = "发送状态",hidden = true)
    private Integer sendStatus;

}
