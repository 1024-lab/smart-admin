package net.lab1024.sa.base.module.support.dict.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 数据字典 新建表单
 *
 * @Author 1024创新实验室-主任-卓大
 * @Date 2025-03-25 22:25:04
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */

@Data
public class DictAddForm {

    @Schema(description = "字典名字", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典名字 不能为空")
    private String dictName;

    @Schema(description = "字典编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典编码 不能为空")
    private String dictCode;

    @Schema(description = "字典备注")
    private String remark;

}