package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RelayModelAddForm {

    @Schema(description = "模型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "模型名称不能为空")
    private String modelName;

    @Schema(description = "端点类型(1=Chat,2=Embedding,3=Both)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "端点类型不能为空")
    private Integer endpointType;

    @Schema(description = "输入价格(每1k tokens)")
    private BigDecimal inputPricePerK;

    @Schema(description = "输出价格(每1k tokens)")
    private BigDecimal outputPricePerK;

    @Schema(description = "备注")
    private String remark;
}
