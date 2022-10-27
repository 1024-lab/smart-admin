package net.lab1024.smartadmin.module.business.log.userloginlog.domain;

import net.lab1024.smartadmin.common.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [ 用户登录日志 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date 2019-05-15 10:25:21
 * @since JDK1.8
 */
@Data
public class UserLoginLogQueryDTO extends PageParamDTO {


    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;


    @ApiModelProperty("用户名")
    private String userName;

}
