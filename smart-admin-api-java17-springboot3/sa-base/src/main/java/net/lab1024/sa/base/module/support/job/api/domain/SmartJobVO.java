package net.lab1024.sa.base.module.support.job.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.json.serializer.enumeration.EnumSerialize;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.module.support.job.constant.SmartJobTriggerTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务 vo
 *
 * @author huke
 * @date 2024/6/17 21:30
 */
@Data
public class SmartJobVO {

    @Schema(description = "任务id")
    private Integer jobId;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "执行类")
    private String jobClass;

    @SchemaEnum(desc = "触发类型", value = SmartJobTriggerTypeEnum.class)
    @EnumSerialize(SmartJobTriggerTypeEnum.class)
    private String triggerType;

    @Schema(description = "触发配置")
    private String triggerValue;

    @Schema(description = "定时任务参数|可选")
    private String param;

    @Schema(description = "是否启用")
    private Boolean enabledFlag;

    @Schema(description = "最后一执行时间")
    private LocalDateTime lastExecuteTime;

    @Schema(description = "最后一次执行记录id")
    private Long lastExecuteLogId;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序")
    private Integer sort;

    private String updateName;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    @Schema(description = "上次执行记录")
    private SmartJobLogVO lastJobLog;

    @Schema(description = "未来N次任务执行时间")
    private List<LocalDateTime> nextJobExecuteTimeList;
}
