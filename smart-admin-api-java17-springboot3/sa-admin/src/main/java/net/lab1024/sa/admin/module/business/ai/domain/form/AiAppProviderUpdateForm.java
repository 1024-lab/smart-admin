package net.lab1024.sa.admin.module.business.ai.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 应用提供商 更新表单
 *
 * @Author Mission
 * @Date 2025-12-21 16:06:19
 * @Copyright Mission
 */

@Data
public class AiAppProviderUpdateForm {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "id 不能为空")
    private String id;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "名称 不能为空")
    private String name;

    @Schema(description = "baseUrl", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "baseUrl 不能为空")
    private String baseUrl;

    @Schema(description = "key", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "key 不能为空")
    private String key;

}