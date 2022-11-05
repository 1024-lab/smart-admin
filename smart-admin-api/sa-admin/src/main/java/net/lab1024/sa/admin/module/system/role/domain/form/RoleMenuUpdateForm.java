package net.lab1024.sa.admin.module.system.role.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色的菜单更新
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-04-08 21:53:04
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class RoleMenuUpdateForm {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    /**
     * 菜单ID 集合
     */
    @ApiModelProperty("菜单ID集合")
    @NotNull(message = "菜单ID不能为空")
    private List<Long> menuIdList;

}
