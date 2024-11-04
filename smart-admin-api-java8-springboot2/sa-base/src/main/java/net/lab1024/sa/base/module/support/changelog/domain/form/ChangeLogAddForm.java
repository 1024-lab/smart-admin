package net.lab1024.sa.base.module.support.changelog.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.base.module.support.changelog.constant.ChangeLogTypeEnum;

/**
 * 系统更新日志 新建表单
 *
 * @Author 卓大
 * @Date 2022-09-26 14:53:50
 * @Copyright 1024创新实验室
 */

@Data
public class ChangeLogAddForm {

    @Schema(description = "版本", required = true)
    @NotBlank(message = "版本 不能为空")
    private String version;

    @SchemaEnum(value = ChangeLogTypeEnum.class, desc = "更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]")
    @CheckEnum(value = ChangeLogTypeEnum.class, message = "更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复] 错误", required = true)
    private Integer type;

    @Schema(description = "发布人", required = true)
    @NotBlank(message = "发布人 不能为空")
    private String publishAuthor;

    @Schema(description = "发布日期", required = true)
    @NotNull(message = "发布日期 不能为空")
    private LocalDate publicDate;

    @Schema(description = "更新内容", required = true)
    @NotBlank(message = "更新内容 不能为空")
    private String content;

    @Schema(description = "跳转链接")
    private String link;

}