package net.lab1024.sa.admin.module.system.menu.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.admin.module.system.menu.constant.MenuPermsTypeEnum;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.sa.common.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;
import net.lab1024.sa.admin.module.system.menu.constant.MenuTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单基础
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-03-06 22:04:37
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class MenuBaseForm {

    @ApiModelProperty("菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    @Length(max = 30, message = "菜单名称最多30个字符")
    private String menuName;

    @ApiModelPropertyEnum(value = MenuTypeEnum.class, desc = "类型")
    @CheckEnum(value = MenuTypeEnum.class, message = "类型错误")
    private Integer menuType;

    @ApiModelProperty("父菜单ID 无上级可传0")
    @NotNull(message = "父菜单ID不能为空")
    private Long parentId;

    @ApiModelProperty("显示顺序")
    private Integer sort;

    @ApiModelProperty("路由地址")
    private String path;

    @ApiModelProperty("组件路径")
    private String component;

    @ApiModelProperty("是否为外链")
    @NotNull(message = "是否为外链不能为空")
    private Boolean frameFlag;

    @ApiModelProperty("外链地址")
    private String frameUrl;

    @ApiModelProperty("是否缓存")
    @NotNull(message = "是否缓存不能为空")
    private Boolean cacheFlag;

    @ApiModelProperty("显示状态")
    @NotNull(message = "显示状态不能为空")
    private Boolean visibleFlag;

    @ApiModelProperty("禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ApiModelPropertyEnum(value = MenuPermsTypeEnum.class, desc = "权限类型 ")
    @CheckEnum(value = MenuPermsTypeEnum.class, message = "权限类型")
    private Integer permsType;

    @ApiModelProperty("前端权限字符串")
    private String webPerms;

    @ApiModelProperty("后端端权限字符串")
    private String apiPerms;

    @ApiModelProperty("后端接口权限集合（拆分）")
    private List<String> apiPermsList;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("功能点关联菜单ID")
    private Long contextMenuId;
}
