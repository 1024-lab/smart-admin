package net.lab1024.sa.base.module.support.job.repository.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.job.constant.SmartJobTriggerTypeEnum;

import java.time.LocalDateTime;

/**
 * 定时任务 实体类
 *
 * @author huke
 * @date 2024/6/17 21:30
 */
@Data
@TableName("t_smart_job")
public class SmartJobEntity {

    /**
     * 任务id
     */
    @TableId(type = IdType.AUTO)
    private Integer jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 执行类
     */
    private String jobClass;

    /**
     * 触发类型
     *
     * @see SmartJobTriggerTypeEnum
     */
    private String triggerType;

    /**
     * 触发配置
     */
    private String triggerValue;

    /**
     * 定时任务参数 可选
     */
    private String param;

    /**
     * 是否启用
     */
    private Boolean enabledFlag;

    /**
     * 最后一执行时间
     */
    private LocalDateTime lastExecuteTime;

    /**
     * 最后一次执行记录id
     */
    private Long lastExecuteLogId;

    /**
     * 备注描述 可选
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;

    private String updateName;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
