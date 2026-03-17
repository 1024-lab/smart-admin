package net.lab1024.sa.admin.module.business.ai.relay.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_ai_quota_ledger")
public class RelayQuotaLedgerEntity {

    @TableId(value = "ledger_id", type = IdType.ASSIGN_ID)
    private Long ledgerId;

    private Long employeeId;

    private Long tokenId;

    private Long billingSessionId;

    private String requestId;

    private Integer actionType;

    private BigDecimal deltaQuota;

    private BigDecimal balanceAfter;

    private LocalDateTime createTime;
}
