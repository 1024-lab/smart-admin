package net.lab1024.sa.common.module.support.changelog.domain.form;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.sa.common.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.common.module.support.changelog.constant.ChangeLogTypeEnum;

/**
 * 系统更新日志 新建表单
 *
 * @Author 卓大
 * @Date 2022-09-26 14:53:50
 * @Copyright 1024创新实验室
 */

@Data
public class ChangeLogAddForm {

    @ApiModelProperty(value = "版本", required = true)
    @NotBlank(message = "版本 不能为空")
    private String version;

    @ApiModelPropertyEnum(value = ChangeLogTypeEnum.class, desc = "更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]")
    @CheckEnum(value = ChangeLogTypeEnum.class, message = "更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复] 错误", required = true)
    private Integer type;

    @ApiModelProperty(value = "发布人", required = true)
    @NotBlank(message = "发布人 不能为空")
    private String publishAuthor;

    @ApiModelProperty(value = "发布日期", required = true)
    @NotNull(message = "发布日期 不能为空")
    private LocalDate publicDate;

    @ApiModelProperty(value = "更新内容", required = true)
    @NotBlank(message = "更新内容 不能为空")
    private String content;

    @ApiModelProperty(value = "跳转链接")
    private String link;

}