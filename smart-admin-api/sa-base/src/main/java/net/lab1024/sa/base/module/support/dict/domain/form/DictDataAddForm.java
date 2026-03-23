package net.lab1024.sa.base.module.support.dict.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 字典数据表 新建表单
 *
 * @Author 1024创新实验室-主任-卓大
 * @Date 2025-03-25 23:12:59
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */

@Data
public class DictDataAddForm {

    @Schema(description = "字典id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "字典id 不能为空")
    private Long dictId;

    @Schema(description = "字典项值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典项值 不能为空")
    private String dataValue;

    @Schema(description = "字典项显示名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典项显示名称 不能为空")
    private String dataLabel;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序（越大越靠前）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序（越大越靠前） 不能为空")
    private Integer sortOrder;

}