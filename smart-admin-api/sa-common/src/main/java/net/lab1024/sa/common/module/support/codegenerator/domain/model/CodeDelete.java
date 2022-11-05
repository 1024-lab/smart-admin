package net.lab1024.sa.common.module.support.codegenerator.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.sa.common.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.common.module.support.codegenerator.constant.CodeDeleteEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 代码生成 删除 模型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-30 22:15:38
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Data
public class CodeDelete {

    @ApiModelProperty("是否支持删除 ")
    @NotNull(message = "4.删除 是否支持删除 不能为空")
    private Boolean isSupportDelete;

    @ApiModelProperty("是否为物理删除")
    @NotNull(message = "4.删除 是否为物理删除 不能为空")
    private Boolean isPhysicallyDeleted;

    @ApiModelProperty("删除类型")
    @NotBlank(message = "4.删除 删除类型 不能为空")
    @ApiModelPropertyEnum(CodeDeleteEnum.class)
    @CheckEnum(value = CodeDeleteEnum.class, message = "删除 删除类型 枚举值错误")
    private String deleteEnum;


}
