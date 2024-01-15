package net.lab1024.sa.base.module.support.codegenerator.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.base.module.support.codegenerator.constant.CodeQueryFieldQueryTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 代码生成 查询条件 模型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-30 22:15:38
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */

@Data
public class CodeQueryField {

    @NotBlank(message = "5、查询条件 条件名称 不能为空")
    @Schema(description = "条件名称")
    private String label;

    @NotBlank(message = "5、查询条件 字段名 不能为空")
    @Schema(description = "字段名")
    private String fieldName;

    @SchemaEnum(CodeQueryFieldQueryTypeEnum.class)
    @CheckEnum(value = CodeQueryFieldQueryTypeEnum.class, message = "5、查询条件 查询条件 查询类型 枚举值错误")
    private String queryTypeEnum;

    @NotEmpty(message = "5、查询条件 列 不能为空")
    @Schema(description = "列")
    private List<String> columnNameList;

    @NotBlank(message = "5、查询条件 宽度 不能为空")
    @Schema(description = "宽度")
    private String width;


}
