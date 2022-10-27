package net.lab1024.sa.admin.module.system.menu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 简易的菜单VO
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-03-06 22:04:37
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class MenuSimpleTreeVO {

    @ApiModelProperty("菜单ID")
    private Long menuId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("功能点关联菜单ID")
    private Long contextMenuId;

    @ApiModelProperty("父级菜单ID")
    private Long parentId;

    @ApiModelProperty("菜单类型")
    private Integer menuType;

    @ApiModelProperty("子菜单")
    private List<MenuSimpleTreeVO> children;
}
