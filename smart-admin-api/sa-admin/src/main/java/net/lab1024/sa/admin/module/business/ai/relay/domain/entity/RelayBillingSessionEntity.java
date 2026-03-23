package net.lab1024.sa.admin.module.business.ai.relay.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_ai_billing_session")
public class RelayBillingSessionEntity {

    @TableId(value = "billing_session_id", type = IdType.ASSIGN_ID)
    private Long billingSessionId;

    private String requestId;

    private Long employeeId;

    private Long tokenId;

    private Long channelId;

    private Long modelId;

    private Integer endpointType;

    private BigDecimal preconsumeQuota;

    private BigDecimal settledQuota;

    private BigDecimal deltaQuota;

    private Integer billingState;

    private Integer upstreamStatusCode;

    private Integer gatewayStatusCode;

    private Long latencyMs;

    private String errorMsg;

    private LocalDateTime settledTime;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
