package net.lab1024.sa.base.module.support.job.repository.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 定时任务 执行记录 实体类
 *
 * @author huke
 * @date 2024/6/17 21:30
 */
@Data
@TableName("t_smart_job_log")
public class SmartJobLogEntity {

    @TableId(type = IdType.AUTO)
    private Long logId;

    /**
     * 任务id
     */
    private Integer jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 定时任务参数 可选
     */
    private String param;

    /**
     * 执行结果 是否成功
     */
    private Boolean successFlag;

    /**
     * 开始执行时间
     */
    private LocalDateTime executeStartTime;

    /**
     * 执行时长-毫秒
     */
    private Long executeTimeMillis;

    /**
     * 执行结束时间
     */
    private LocalDateTime executeEndTime;

    /**
     * 执行结果 描述 可选
     */
    private String executeResult;

    /**
     * ip
     */
    private String ip;

    /**
     * 进程id
     */
    private String processId;

    /**
     * 程序目录
     */
    private String programPath;

    private String createName;

    private LocalDateTime createTime;
}
