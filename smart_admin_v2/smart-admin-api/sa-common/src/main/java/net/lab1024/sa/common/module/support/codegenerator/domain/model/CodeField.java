package net.lab1024.sa.common.module.support.codegenerator.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 代码生成 基础字段 模型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-30 22:15:38
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Data
public class CodeField {

    @ApiModelProperty("列")
    @NotBlank(message = "列名 不能为空")
    private String columnName;

    @ApiModelProperty("列备注")
    private String columnComment;

    @ApiModelProperty("业务名称")
    @NotBlank(message = "业务名称 不能为空")
    private String label;

    @ApiModelProperty("字段")
    @NotBlank(message = "字段 不能为空")
    private String fieldName;

    @ApiModelProperty("java类型")
    @NotBlank(message = "java类型 不能为空")
    private String javaType;

    @ApiModelProperty("js类型")
    @NotBlank(message = "js类型 不能为空")
    private String jsType;

    @ApiModelProperty("字典key")
    private String dict;

    @ApiModelProperty("枚举名称")
    private String enumName;

    @ApiModelProperty("主键")
    @NotNull(message = "主键 不能为空")
    private Boolean primaryKeyFlag;

    @ApiModelProperty("自增")
    @NotNull(message = "自增 不能为空")
    private Boolean autoIncreaseFlag;

}
