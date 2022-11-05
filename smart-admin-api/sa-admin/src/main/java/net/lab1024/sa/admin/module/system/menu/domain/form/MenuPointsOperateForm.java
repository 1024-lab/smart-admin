package net.lab1024.sa.admin.module.system.menu.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单功能点操作Form
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-03-06 22:04:37
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class MenuPointsOperateForm {

    @ApiModelProperty("菜单ID")
    private Long menuId;

    @ApiModelProperty("功能点名称")
    @NotBlank(message = "功能点不能为空")
    @Length(max = 30, message = "功能点最多30个字符")
    private String menuName;

    @ApiModelProperty("禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ApiModelProperty("后端接口权限集合")
    private List<String> apiPermsList;

    @ApiModelProperty("权限字符串")
    private String webPerms;

    @ApiModelProperty("功能点关联菜单ID")
    private Long contextMenuId;
}
