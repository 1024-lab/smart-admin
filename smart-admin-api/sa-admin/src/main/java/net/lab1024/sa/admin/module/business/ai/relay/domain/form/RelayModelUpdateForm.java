package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RelayModelUpdateForm extends RelayModelAddForm {

    @Schema(description = "模型ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "模型ID不能为空")
    private Long modelId;
}
