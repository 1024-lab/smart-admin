package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class RelayUsageLogQueryForm extends PageParam {

    @Schema(description = "员工ID")
    private Long employeeId;

    @Schema(description = "请求ID")
    private String requestId;

    @Schema(description = "模型ID")
    private Long modelId;

    @Schema(description = "渠道ID")
    private Long channelId;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}
