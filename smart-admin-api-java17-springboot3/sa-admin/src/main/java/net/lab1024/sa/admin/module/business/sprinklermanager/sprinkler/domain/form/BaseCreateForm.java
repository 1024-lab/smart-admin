package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseCreateForm {

    @ExcelIgnore
    @Schema(description = "禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ExcelIgnore
    @Schema(description = "创建人", hidden = true)
    private Long createUserId;

    @ExcelIgnore
    @Schema(description = "创建人", hidden = true)
    private String createUserName;
}
