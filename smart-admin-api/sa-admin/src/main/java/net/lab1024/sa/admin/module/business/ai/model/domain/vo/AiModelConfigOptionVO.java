package net.lab1024.sa.admin.module.business.ai.model.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AiModelConfigOptionVO {

    @Schema(description = "模型配置ID")
    private Long id;

    @Schema(description = "供应商")
    private String provider;

    @Schema(description = "模型名称")
    private String modelName;

    @Schema(description = "模型编码")
    private String endPointModeName;

    @Schema(description = "模型类型")
    private String modelType;

    @Schema(description = "能力标签（逗号分隔）")
    private String modelFeatures;
}
