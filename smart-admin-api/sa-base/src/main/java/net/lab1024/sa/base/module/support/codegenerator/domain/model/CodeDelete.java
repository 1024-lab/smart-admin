package net.lab1024.sa.base.module.support.codegenerator.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.base.module.support.codegenerator.constant.CodeDeleteEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 代码生成 删除 模型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-30 22:15:38
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */

@Data
public class CodeDelete {

    @Schema(description = "是否支持删除 ")
    @NotNull(message = "4.删除 是否支持删除 不能为空")
    private Boolean isSupportDelete;

    @Schema(description = "是否为物理删除")
    @NotNull(message = "4.删除 是否为物理删除 不能为空")
    private Boolean isPhysicallyDeleted;

    @Schema(description = "删除类型")
    @NotBlank(message = "4.删除 删除类型 不能为空")
    @SchemaEnum(CodeDeleteEnum.class)
    @CheckEnum(value = CodeDeleteEnum.class, message = "删除 删除类型 枚举值错误")
    private String deleteEnum;


}
