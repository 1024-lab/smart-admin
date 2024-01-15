package net.lab1024.sa.base.module.support.codegenerator.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.base.module.support.codegenerator.constant.CodeGeneratorPageTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 代码生成 增加、修改 模型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-30 22:15:38
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */

@Data
public class CodeInsertAndUpdate {

    @NotNull(message = "3.增加、修改 是否支持增加、修改 不能为空")
    private Boolean isSupportInsertAndUpdate;

    @SchemaEnum(CodeGeneratorPageTypeEnum.class)
    @CheckEnum(value = CodeGeneratorPageTypeEnum.class, message = "3.增加、修改 增加、修改 页面类型 枚举值错误")
    private String pageType;

    @Schema(description = "宽度")
    private String width;

    @NotNull(message = "3.增加、修改 每行字段数量 不能为空")
    @Schema(description = "每行字段数量")
    private Integer countPerLine;

    @Schema(description = "字段列表")
    private List<CodeInsertAndUpdateField> fieldList;

}
