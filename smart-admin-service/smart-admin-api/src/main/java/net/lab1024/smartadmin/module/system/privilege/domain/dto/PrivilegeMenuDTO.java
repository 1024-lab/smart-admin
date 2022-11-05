package net.lab1024.smartadmin.module.system.privilege.domain.dto;

import net.lab1024.smartadmin.common.anno.ApiModelPropertyEnum;
import net.lab1024.smartadmin.module.system.privilege.constant.PrivilegeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/8/20 0020 下午 16:32
 * @since JDK1.8
 */
@Data
public class PrivilegeMenuDTO {

    @ApiModelPropertyEnum(enumDesc = "菜单类型",value = PrivilegeTypeEnum.class)
    @NotNull
    private Integer type;

    @ApiModelProperty("菜单名")
    @NotNull(message = "菜单名不能为空")
    private String menuName;

    @ApiModelProperty("菜单Key")
    @NotNull(message = "菜单Key不能为空")
    private String menuKey;

    @ApiModelProperty("父级菜单Key,根节点不传")
    private String parentKey;

    @ApiModelProperty("前端路由path")
    @NotNull(message = "前端路由path不能为空")
    private String url;

    @ApiModelProperty("排序字段")
    @NotNull(message = "菜单项顺序不能为空")
    private Integer sort;
}
