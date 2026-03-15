package net.lab1024.sa.admin.module.business.ai.relay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RelayQuotaLedgerVO {

    @Schema(description = "流水ID")
    private Long ledgerId;

    @Schema(description = "员工ID")
    private Long employeeId;

    @Schema(description = "Token ID")
    private Long tokenId;

    @Schema(description = "计费会话ID")
    private Long billingSessionId;

    @Schema(description = "请求ID")
    private String requestId;

    @Schema(description = "操作类型")
    private Integer actionType;

    @Schema(description = "变动额度")
    private Long deltaQuota;

    @Schema(description = "变动后余额")
    private Long balanceAfter;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
