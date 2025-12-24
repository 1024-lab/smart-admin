package net.lab1024.sa.admin.module.business.ai.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ai应用管理表 新建表单
 *
 * @Author Mission
 * @Date 2025-12-21 16:00:44
 * @Copyright Mission
 */

@Data
public class AiAppAddForm {

    @Schema(description = "id")
    private String id;

    @Schema(description = "应用名称")
    @NotBlank(message = "应用名称 不能为空")
    private String name;

    @Schema(description = "应用描述")
    @NotBlank(message = "应用描述 不能为空")
    private String description;

    @Schema(description = "应用提供商id")
    @NotBlank(message = "应用提供商id 不能为空")
    private String appProviderId;

}