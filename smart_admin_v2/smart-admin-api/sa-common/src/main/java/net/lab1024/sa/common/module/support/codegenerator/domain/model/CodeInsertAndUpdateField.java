package net.lab1024.sa.common.module.support.codegenerator.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.sa.common.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.common.module.support.codegenerator.constant.CodeFrontComponentEnum;
import net.lab1024.sa.common.module.support.codegenerator.constant.CodeGeneratorPageTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 代码生成 增加、修改的字段 模型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-30 22:15:38
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Data
public class CodeInsertAndUpdateField {

    @NotBlank(message = "3.增加、修改 列名 不能为空")
    @ApiModelProperty("列名")
    private String columnName;

    @NotNull(message = "3.增加、修改  必须 不能为空")
    @ApiModelProperty("必须")
    private Boolean requiredFlag;

    @NotNull(message = "3.增加、修改  插入标识 不能为空")
    @ApiModelProperty("插入标识")
    private Boolean insertFlag;

    @NotNull(message = "3.增加、修改  更新标识 不能为空")
    @ApiModelProperty("更新标识")
    private Boolean updateFlag;

    @ApiModelPropertyEnum(value = CodeGeneratorPageTypeEnum.class)
    @CheckEnum(value = CodeFrontComponentEnum.class, message = "3.增加、修改  增加、修改 组件类型 枚举值错误", required = true)
    private String frontComponent;

}
