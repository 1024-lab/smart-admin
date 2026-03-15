package net.lab1024.sa.admin.module.business.ai.relay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RelayUsageLogVO {

    @Schema(description = "用量日志ID")
    private Long usageLogId;

    @Schema(description = "请求ID")
    private String requestId;

    @Schema(description = "计费会话ID")
    private Long billingSessionId;

    @Schema(description = "员工ID")
    private Long employeeId;

    @Schema(description = "Token ID")
    private Long tokenId;

    @Schema(description = "渠道ID")
    private Long channelId;

    @Schema(description = "渠道名称")
    private String channelName;

    @Schema(description = "模型ID")
    private Long modelId;

    @Schema(description = "模型名称")
    private String modelName;

    @Schema(description = "端点类型")
    private Integer endpointType;

    @Schema(description = "是否流式")
    private Integer streamFlag;

    @Schema(description = "输入tokens")
    private Integer promptTokens;

    @Schema(description = "输出tokens")
    private Integer completionTokens;

    @Schema(description = "总tokens")
    private Integer totalTokens;

    @Schema(description = "输入费用")
    private BigDecimal inputCost;

    @Schema(description = "输出费用")
    private BigDecimal outputCost;

    @Schema(description = "总费用")
    private BigDecimal totalCost;

    @Schema(description = "预扣额度")
    private Long preconsumeQuota;

    @Schema(description = "结算额度")
    private Long settledQuota;

    @Schema(description = "上游状态码")
    private Integer upstreamStatus;

    @Schema(description = "网关状态码")
    private Integer gatewayStatus;

    @Schema(description = "延迟(ms)")
    private Long latencyMs;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
