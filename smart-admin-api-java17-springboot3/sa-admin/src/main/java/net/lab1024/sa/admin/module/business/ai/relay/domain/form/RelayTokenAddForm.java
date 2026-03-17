package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RelayTokenAddForm {

    @Schema(description = "员工ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "员工ID不能为空")
    private Long employeeId;

    @Schema(description = "渠道组ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "渠道组不能为空")
    private Long channelGroupId;

    @Schema(description = "Token名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Token名称不能为空")
    private String tokenName;

    @Schema(description = "过期时间")
    private LocalDateTime expiresTime;

    @Schema(description = "备注")
    private String remark;
}
