package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RelayTokenUpdateForm {

    @Schema(description = "Token ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Token ID不能为空")
    private Long tokenId;

    @Schema(description = "Token名称")
    private String tokenName;

    @Schema(description = "过期时间")
    private LocalDateTime expiresTime;

    @Schema(description = "是否禁用")
    private Integer disabledFlag;

    @Schema(description = "备注")
    private String remark;
}
