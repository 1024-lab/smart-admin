package net.lab1024.smartadmin.module.support.quartz.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import net.lab1024.smartadmin.common.domain.BaseEntity;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/13 0013 下午 13:45
 * @since JDK1.8
 */
@Data
@TableName("t_quartz_task_log")
public class QuartzTaskLogEntity extends BaseEntity {
    /**
     * 任务名称参数
     */
    private Long taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务参数
     */
    private String taskParams;
    /**
     * 任务处理状态
     */
    private Integer processStatus;

    /**
     * 任务时长ms
     */
    private Long processDuration;

    /**
     * 处理日志
     */
    private String processLog;


    private String ipAddress;

}
