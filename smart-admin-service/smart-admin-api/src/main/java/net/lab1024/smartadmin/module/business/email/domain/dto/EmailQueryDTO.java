package net.lab1024.smartadmin.module.business.email.domain.dto;

import net.lab1024.smartadmin.common.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date 2019-05-13 17:10:16
 * @since JDK1.8
 */
@Data
public class EmailQueryDTO extends PageParamDTO {


    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;


    @ApiModelProperty("标题")
    private String title;


    @ApiModelProperty("发送状态 0未发送 1已发送")
    private Integer sendStatus;

}
