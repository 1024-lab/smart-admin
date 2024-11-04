package net.lab1024.sa.base.module.support.codegenerator.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 代码生成 预览 表单
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022/6/23 23:20:46
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class CodeGeneratorPreviewForm {

    @NotBlank(message = "模板文件 不能为空")
    @Schema(description = "模板文件")
    private String templateFile;

    @NotBlank(message = "表名 不能为空")
    @Schema(description = "表名")
    private String tableName;

}
