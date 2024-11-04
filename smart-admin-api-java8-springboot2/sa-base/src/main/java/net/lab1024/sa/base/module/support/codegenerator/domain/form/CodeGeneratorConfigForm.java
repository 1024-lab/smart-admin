package net.lab1024.sa.base.module.support.codegenerator.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.lab1024.sa.base.module.support.codegenerator.domain.model.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 代码生成 配置信息表单
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-29 20:23:46
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeGeneratorConfigForm {

    @NotBlank(message = "表名 不能为空")
    @Schema(description = "表名")
    private String tableName;

    @Valid
    @NotNull(message = "基础信息不能为空")
    @Schema(description = "基础信息")
    private CodeBasic basic;

    @Valid
    @NotNull(message = "字段信息不能为空")
    @Schema(description = "字段信息")
    private List<CodeField> fields;

    @Valid
    @NotNull(message = "增加、修改 信息 不能为空")
    @Schema(description = "增加、修改 信息")
    private CodeInsertAndUpdate insertAndUpdate;

    @Valid
    @NotNull(message = "删除 信息 不能为空")
    @Schema(description = "删除 信息")
    private CodeDelete deleteInfo;

    @Valid
    @Schema(description = "查询字段")
    private List<CodeQueryField> queryFields;

    @Valid
    @Schema(description = "列表字段")
    private List<CodeTableField> tableFields;

}
