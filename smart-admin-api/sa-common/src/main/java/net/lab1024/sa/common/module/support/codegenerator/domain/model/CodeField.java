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
    @NotBlank(message = " 2.字段列表 列名 不能为空")
    private String columnName;

    @ApiModelProperty("列备注")
    private String columnComment;

    @ApiModelProperty("字段名词")
    @NotBlank(message = "2.字段列表 字段名词 不能为空")
    private String label;

    @ApiModelProperty("字段命名")
    @NotBlank(message = "2.字段列表 字段命名 不能为空")
    private String fieldName;

    @ApiModelProperty("java类型")
    @NotBlank(message = "2.字段列表 java类型 不能为空")
    private String javaType;

    @ApiModelProperty("js类型")
    @NotBlank(message = "2.字段列表 js类型 不能为空")
    private String jsType;

    @ApiModelProperty("字典key")
    private String dict;

    @ApiModelProperty("枚举名称")
    private String enumName;

    @ApiModelProperty("主键")
    @NotNull(message = "2.字段列表 主键 不能为空")
    private Boolean primaryKeyFlag;

    @ApiModelProperty("自增")
    @NotNull(message = "2.字段列表 自增 不能为空")
    private Boolean autoIncreaseFlag;

}
