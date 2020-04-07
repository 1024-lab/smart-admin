package net.lab1024.smartadmin.module.system.role.roleprivilege;

import net.lab1024.smartadmin.common.anno.OperateLog;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.module.system.role.roleprivilege.domain.dto.RolePrivilegeDTO;
import net.lab1024.smartadmin.module.system.role.roleprivilege.domain.dto.RolePrivilegeTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * [ 与员工权限相关：角色权限关系、权限列表 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@OperateLog
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_ROLE_PRIVILEGE})
public class RolePrivilegeController {

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @ApiOperation(value = "更新角色权限", notes = "更新角色权限")
    @PostMapping("/privilege/updateRolePrivilege")
    public ResponseDTO<String> updateRolePrivilege(@Valid @RequestBody RolePrivilegeDTO updateDTO) {
        return rolePrivilegeService.updateRolePrivilege(updateDTO);
    }

    @ApiOperation(value = "获取角色可选的功能权限", notes = "获取角色可选的功能权限")
    @GetMapping("/privilege/listPrivilegeByRoleId/{roleId}")
    public ResponseDTO<RolePrivilegeTreeVO> listPrivilegeByRoleId(@PathVariable("roleId") Long roleId) {
        return rolePrivilegeService.listPrivilegeByRoleId(roleId);
    }

}
