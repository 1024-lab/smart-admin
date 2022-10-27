package net.lab1024.smartadmin.module.support.quartz.domain.dto;

import net.lab1024.smartadmin.common.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/15 0015 上午 11:29
 * @since JDK1.8
 */
@Data
public class QuartzLogQueryDTO extends PageParamDTO {

    @ApiModelProperty(value = "任务Id(不能为空)")
    @NotNull(message = "任务Id不能为空")
    private Integer taskId;
}
