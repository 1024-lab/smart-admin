package net.lab1024.sa.admin.module.business.ai.relay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RelayUserAccountVO {

    @Schema(description = "员工ID")
    private Long employeeId;

    @Schema(description = "员工姓名")
    private String employeeName;

    @Schema(description = "剩余额度")
    private BigDecimal quotaRemain;

    @Schema(description = "已用额度")
    private BigDecimal quotaUsed;

    @Schema(description = "是否禁用")
    private Integer disabledFlag;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
