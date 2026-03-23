package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RelayChannelUpdateForm extends RelayChannelAddForm {

    @Schema(description = "渠道ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "渠道ID不能为空")
    private Long channelId;

    @Schema(description = "API Key(为空则不更新)")
    private String apiKey;
}
