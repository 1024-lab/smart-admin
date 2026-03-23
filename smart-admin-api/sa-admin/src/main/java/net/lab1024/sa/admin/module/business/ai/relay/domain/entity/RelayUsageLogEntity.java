package net.lab1024.sa.admin.module.business.ai.relay.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_ai_usage_log")
public class RelayUsageLogEntity {

    @TableId(value = "usage_log_id", type = IdType.ASSIGN_ID)
    private Long usageLogId;

    private String requestId;

    private Long billingSessionId;

    private Long employeeId;

    private Long tokenId;

    private Long channelId;

    private Long modelId;

    private Integer endpointType;

    private Integer streamFlag;

    private Integer promptTokens;

    private Integer completionTokens;

    private Integer totalTokens;

    private BigDecimal inputCost;

    private BigDecimal outputCost;

    private BigDecimal totalCost;

    private BigDecimal preconsumeQuota;

    private BigDecimal settledQuota;

    private Integer upstreamStatusCode;

    private Integer gatewayStatusCode;

    private Long latencyMs;

    private String errorMsg;

    private LocalDateTime createTime;
}
