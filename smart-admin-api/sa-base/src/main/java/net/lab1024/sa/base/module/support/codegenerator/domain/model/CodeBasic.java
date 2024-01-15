package net.lab1024.sa.base.module.support.codegenerator.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 代码生成 基础数据 模型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-30 22:15:38
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */

@Data
public class CodeBasic {

    @Schema(description = "业务名称")
    @NotBlank(message = "1.基础命名 基础命名 不能为空")
    private String moduleName;

    @Schema(description = "java包名")
    @NotBlank(message = "1.基础命名 java包名 不能为空")
    private String javaPackageName;

    @Schema(description = "注释")
    @NotBlank(message = "1.基础命名 注释 不能为空")
    private String description;

    @Schema(description = "前端作者")
    @NotBlank(message = "1.基础命名 前端作者 不能为空")
    private String frontAuthor;

    @Schema(description = "前端时间")
    @NotNull(message = "1.基础命名 前端时间 不能为空")
    private LocalDateTime frontDate;

    @Schema(description = "后端作者")
    @NotBlank(message = "1.基础命名 后端作者 不能为空")
    private String backendAuthor;

    @Schema(description = "后端时间")
    @NotNull(message = "1.基础命名 后端时间 不能为空")
    private LocalDateTime backendDate;

    @Schema(description = "版权信息")
    @NotNull(message = "1.基础命名 版权信息 不能为空")
    private String copyright;

}
