package net.lab1024.smartadmin.module.support.quartz.domain.dto;

import net.lab1024.smartadmin.module.support.quartz.constant.TaskStatusEnum;
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
 * @date 2019/4/13 0013 下午 15:42
 * @since JDK1.8
 */
@Data
public class QuartzTaskDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("任务名称")
    @NotNull(message = "任务名称不能为空")
    private String taskName;

    @ApiModelProperty("任务Bean")
    @NotNull(message = "任务Bean不能为空")
    private String taskBean;

    @ApiModelProperty("任务参数")
    private String taskParams;

    @ApiModelProperty("cron")
    @NotNull(message = "cron表达式不能为空")
    private String taskCron;

    @ApiModelProperty("任务状态:"+ TaskStatusEnum.INFO)
    private Integer taskStatus;

    @ApiModelProperty("任务备注")
    private String remark;
}
