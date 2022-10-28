package net.lab1024.sa.common.module.support.codegenerator.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.sa.common.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.common.module.support.codegenerator.constant.CodeGeneratorPageTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 代码生成 增加、修改 模型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-30 22:15:38
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */

@Data
public class CodeInsertAndUpdate {

    @NotNull(message = "3.增加、修改 是否支持增加、修改 不能为空")
    private Boolean isSupportInsertAndUpdate;

    @ApiModelPropertyEnum(CodeGeneratorPageTypeEnum.class)
    @CheckEnum(value = CodeGeneratorPageTypeEnum.class, message = "3.增加、修改 增加、修改 页面类型 枚举值错误")
    private String pageType;

    @ApiModelProperty("宽度")
    private String width;

    @NotNull(message = "3.增加、修改 每行字段数量 不能为空")
    @ApiModelProperty("每行字段数量")
    private Integer countPerLine;

    @ApiModelProperty("字段列表")
    private List<CodeInsertAndUpdateField> fieldList;

}
