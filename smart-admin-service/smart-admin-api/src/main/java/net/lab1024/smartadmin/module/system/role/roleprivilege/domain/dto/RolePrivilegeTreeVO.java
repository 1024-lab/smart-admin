package net.lab1024.smartadmin.module.system.role.roleprivilege.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RolePrivilegeTreeVO {

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("权限列表")
    private List<RolePrivilegeSimpleDTO> privilege;

    @ApiModelProperty("选中的权限")
    private List<String> selectedKey;
}
