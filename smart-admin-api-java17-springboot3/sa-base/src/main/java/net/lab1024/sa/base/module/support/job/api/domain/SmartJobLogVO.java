package net.lab1024.sa.base.module.support.job.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 定时任务-执行记录 vo
 *
 * @author huke
 * @date 2024/6/17 21:30
 */
@Data
public class SmartJobLogVO {

    @Schema(description = "logId")
    private Long logId;

    @Schema(description = "任务id")
    private Integer jobId;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "定时任务参数|可选")
    private String param;

    @Schema(description = "执行结果是否成功")
    private Boolean successFlag;

    @Schema(description = "开始执行时间")
    private LocalDateTime executeStartTime;

    @Schema(description = "执行时长-毫秒")
    private Long executeTimeMillis;

    @Schema(description = "执行结果描述")
    private String executeResult;

    @Schema(description = "执行结束时间")
    private LocalDateTime executeEndTime;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "进程id")
    private String processId;

    @Schema(description = "程序目录")
    private String programPath;

    private String createName;

    private LocalDateTime createTime;
}
