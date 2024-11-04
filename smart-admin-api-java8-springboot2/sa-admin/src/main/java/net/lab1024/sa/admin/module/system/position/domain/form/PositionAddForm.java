package net.lab1024.sa.admin.module.system.position.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 职务表 新建表单
 *
 * @Author kaiyun
 * @Date 2024-06-23 23:31:38
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */

@Data
public class PositionAddForm {

    @Schema(description = "职务名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "职务名称 不能为空")
    private String positionName;

    @Schema(description = "职级")
    private String level;

    @Schema(description = "排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "备注")
    private String remark;

}