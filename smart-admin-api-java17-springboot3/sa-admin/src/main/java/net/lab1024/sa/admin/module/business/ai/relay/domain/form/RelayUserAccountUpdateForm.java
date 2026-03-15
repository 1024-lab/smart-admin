package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RelayUserAccountUpdateForm {

    @NotNull(message = "员工ID不能为空")
    @Schema(description = "员工ID")
    private Long employeeId;

    @NotNull(message = "剩余额度不能为空")
    @Schema(description = "剩余额度")
    private Long quotaRemain;

    @Schema(description = "是否禁用")
    private Integer disabledFlag;
}
