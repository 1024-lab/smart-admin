package net.lab1024.smartadmin.module.support.quartz.domain.dto;

import net.lab1024.smartadmin.module.support.quartz.constant.TaskResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

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
public class QuartzTaskLogVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("任务id")
    private Long taskId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("任务参数")
    private String taskParams;

    @ApiModelProperty("任务处理状态:"+ TaskResultEnum.INFO)
    private Integer processStatus;

    @ApiModelProperty("任务时长ms")
    private Long processDuration;

    @ApiModelProperty("处理日志")
    private String processLog;

    @ApiModelProperty("创建时间")
    private Date createTime;


    @ApiModelProperty("主机ip")
    private String ipAddress;
}
