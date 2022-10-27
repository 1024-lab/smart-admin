package net.lab1024.sa.admin.module.system.role.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.sa.admin.common.AdminBaseController;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.system.role.domain.form.RoleAddForm;
import net.lab1024.sa.admin.module.system.role.domain.form.RoleUpdateForm;
import net.lab1024.sa.admin.module.system.role.domain.vo.RoleVO;
import net.lab1024.sa.admin.module.system.role.service.RoleService;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021-12-14 19:40:28
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_ROLE})
public class RoleController extends AdminBaseController {

    @Autowired
    private RoleService roleService;

    @ApiOperation("添加角色 @author 卓大")
    @PostMapping("/role/add")
    @PreAuthorize("@saAuth.checkPermission('system:role:add')")
    public ResponseDTO addRole(@Valid @RequestBody RoleAddForm roleAddForm) {
        return roleService.addRole(roleAddForm);
    }

    @ApiOperation("删除角色 @author 卓大")
    @GetMapping("/role/delete/{roleId}")
    @PreAuthorize("@saAuth.checkPermission('system:role:delete')")
    public ResponseDTO<String> deleteRole(@PathVariable Long roleId) {
        return roleService.deleteRole(roleId);
    }

    @ApiOperation("更新角色 @author 卓大")
    @PostMapping("/role/update")
    @PreAuthorize("@saAuth.checkPermission('system:role:update')")
    public ResponseDTO<String> updateRole(@Valid @RequestBody RoleUpdateForm roleUpdateDTO) {
        return roleService.updateRole(roleUpdateDTO);
    }

    @ApiOperation("获取角色数据 @author 卓大")
    @GetMapping("/role/get/{roleId}")
    public ResponseDTO<RoleVO> getRole(@PathVariable("roleId") Long roleId) {
        return roleService.getRoleById(roleId);
    }

    @ApiOperation("获取所有角色 @author 卓大")
    @GetMapping("/role/getAll")
    public ResponseDTO<List<RoleVO>> getAllRole() {
        return roleService.getAllRole();
    }

}
