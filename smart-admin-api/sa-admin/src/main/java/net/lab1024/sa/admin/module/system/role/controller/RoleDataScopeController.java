package net.lab1024.sa.admin.module.system.role.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.sa.admin.common.AdminBaseController;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.system.role.domain.form.RoleDataScopeUpdateForm;
import net.lab1024.sa.admin.module.system.role.domain.vo.RoleDataScopeVO;
import net.lab1024.sa.admin.module.system.role.service.RoleDataScopeService;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色的数据权限配置
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-02-26 22:09:59
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_ROLE_DATA_SCOPE})
public class RoleDataScopeController extends AdminBaseController {

    @Autowired
    private RoleDataScopeService roleDataScopeService;

    @ApiOperation(value = "获取某角色所设置的数据范围 @author 卓大")
    @GetMapping("/role/dataScope/getRoleDataScopeList/{roleId}")
    public ResponseDTO<List<RoleDataScopeVO>> dataScopeListByRole(@PathVariable Long roleId) {
        return roleDataScopeService.getRoleDataScopeList(roleId);
    }

    @ApiOperation(value = "批量设置某角色数据范围 @author 卓大")
    @PostMapping("/role/dataScope/updateRoleDataScopeList")
    @PreAuthorize("@saAuth.checkPermission('system:role:dataScope:update')")
    public ResponseDTO<String> updateRoleDataScopeList(@RequestBody @Valid RoleDataScopeUpdateForm roleDataScopeUpdateForm) {
        return roleDataScopeService.updateRoleDataScopeList(roleDataScopeUpdateForm);
    }


}
