package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RelayUserAccountRechargeForm {

    @Schema(description = "员工ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "员工ID不能为空")
    private Long employeeId;

    @Schema(description = "充值额度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "充值额度不能为空")
    private Long amount;
}
