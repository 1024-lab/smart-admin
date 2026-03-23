package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RelayChannelModelBindForm {

    @Schema(description = "渠道ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "渠道ID不能为空")
    private Long channelId;

    @Schema(description = "模型ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "模型ID列表不能为空")
    private List<Long> modelIds;
}
