package net.lab1024.sa.common.module.support.helpdoc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 帮助文档 目录
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-20 23:11:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class HelpDocCatalogAddForm {

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    @Length(max = 200, message = "名称最多200字符")
    private String name;

    @ApiModelProperty("父级")
    private Long parentId;

    @ApiModelProperty("排序")
    private Integer sort;
}
