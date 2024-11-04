package net.lab1024.sa.admin.module.system.menu.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.admin.module.system.menu.constant.MenuPermsTypeEnum;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;
import net.lab1024.sa.admin.module.system.menu.constant.MenuTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 菜单基础
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-03-06 22:04:37
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class MenuBaseForm {

    @Schema(description = "菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    @Length(max = 30, message = "菜单名称最多30个字符")
    private String menuName;

    @SchemaEnum(value = MenuTypeEnum.class, desc = "类型")
    @CheckEnum(value = MenuTypeEnum.class, message = "类型错误")
    private Integer menuType;

    @Schema(description = "父菜单ID 无上级可传0")
    @NotNull(message = "父菜单ID不能为空")
    private Long parentId;

    @Schema(description = "显示顺序")
    private Integer sort;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "是否为外链")
    @NotNull(message = "是否为外链不能为空")
    private Boolean frameFlag;

    @Schema(description = "外链地址")
    private String frameUrl;

    @Schema(description = "是否缓存")
    @NotNull(message = "是否缓存不能为空")
    private Boolean cacheFlag;

    @Schema(description = "显示状态")
    @NotNull(message = "显示状态不能为空")
    private Boolean visibleFlag;

    @Schema(description = "禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @SchemaEnum(value = MenuPermsTypeEnum.class, desc = "权限类型 ")
    @CheckEnum(value = MenuPermsTypeEnum.class, message = "权限类型")
    private Integer permsType;

    @Schema(description = "前端权限字符串")
    private String webPerms;

    @Schema(description = "后端端权限字符串")
    private String apiPerms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "功能点关联菜单ID")
    private Long contextMenuId;
}
