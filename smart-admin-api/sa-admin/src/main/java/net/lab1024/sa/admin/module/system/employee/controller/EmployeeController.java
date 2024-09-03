package net.lab1024.sa.admin.module.system.employee.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.system.employee.domain.form.*;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.sa.admin.module.system.employee.service.EmployeeService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.apiencrypt.annotation.ApiDecrypt;
import net.lab1024.sa.base.module.support.securityprotect.service.Level3ProtectConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 员工
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021-12-09 22:57:49
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Tag(name = AdminSwaggerTagConst.System.SYSTEM_EMPLOYEE)
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @Resource
    private Level3ProtectConfigService level3ProtectConfigService;

    @PostMapping("/employee/query")
    @Operation(summary = "员工管理查询 @author 卓大")
    public ResponseDTO<PageResult<EmployeeVO>> query(@Valid @RequestBody EmployeeQueryForm query) {
        return employeeService.queryEmployee(query);
    }

    @Operation(summary = "添加员工(返回添加员工的密码) @author 卓大")
    @PostMapping("/employee/add")
    @SaCheckPermission("system:employee:add")
    public ResponseDTO<String> addEmployee(@Valid @RequestBody EmployeeAddForm employeeAddForm) {
        return employeeService.addEmployee(employeeAddForm);
    }

    @Operation(summary = "更新员工 @author 卓大")
    @PostMapping("/employee/update")
    @SaCheckPermission("system:employee:update")
    public ResponseDTO<String> updateEmployee(@Valid @RequestBody EmployeeUpdateForm employeeUpdateForm) {
        return employeeService.updateEmployee(employeeUpdateForm);
    }

    @Operation(summary = "更新登录人信息 @author 善逸")
    @PostMapping("/employee/update/login")
    public ResponseDTO<String> updateByLogin(@Valid @RequestBody EmployeeUpdateForm employeeUpdateForm) {
        employeeUpdateForm.setEmployeeId(SmartRequestUtil.getRequestUserId());
        return employeeService.updateEmployee(employeeUpdateForm);
    }

    @Operation(summary = "更新登录人头像 @author 善逸")
    @PostMapping("/employee/update/avatar")
    public ResponseDTO<String> updateAvatar(@Valid @RequestBody EmployeeUpdateAvatarForm employeeUpdateAvatarForm) {
        employeeUpdateAvatarForm.setEmployeeId(SmartRequestUtil.getRequestUserId());
        return employeeService.updateAvatar(employeeUpdateAvatarForm);
    }

    @Operation(summary = "更新员工禁用/启用状态 @author 卓大")
    @GetMapping("/employee/update/disabled/{employeeId}")
    @SaCheckPermission("system:employee:disabled")
    public ResponseDTO<String> updateDisableFlag(@PathVariable Long employeeId) {
        return employeeService.updateDisableFlag(employeeId);
    }

    @Operation(summary = "批量删除员工 @author 卓大")
    @PostMapping("/employee/update/batch/delete")
    @SaCheckPermission("system:employee:delete")
    public ResponseDTO<String> batchUpdateDeleteFlag(@RequestBody List<Long> employeeIdList) {
        return employeeService.batchUpdateDeleteFlag(employeeIdList);
    }

    @Operation(summary = "批量调整员工部门 @author 卓大")
    @PostMapping("/employee/update/batch/department")
    @SaCheckPermission("system:employee:department:update")
    public ResponseDTO<String> batchUpdateDepartment(@Valid @RequestBody EmployeeBatchUpdateDepartmentForm batchUpdateDepartmentForm) {
        return employeeService.batchUpdateDepartment(batchUpdateDepartmentForm);
    }

    @Operation(summary = "修改密码 @author 卓大")
    @PostMapping("/employee/update/password")
    @ApiDecrypt
    public ResponseDTO<String> updatePassword(@Valid @RequestBody EmployeeUpdatePasswordForm updatePasswordForm) {
        updatePasswordForm.setEmployeeId(SmartRequestUtil.getRequestUserId());
        return employeeService.updatePassword(SmartRequestUtil.getRequestUser(), updatePasswordForm);
    }

    @Operation(summary = "获取密码复杂度 @author 卓大")
    @GetMapping("/employee/getPasswordComplexityEnabled")
    @ApiDecrypt
    public ResponseDTO<Boolean> getPasswordComplexityEnabled() {
        return ResponseDTO.ok(level3ProtectConfigService.isPasswordComplexityEnabled());
    }

    @Operation(summary = "重置员工密码 @author 卓大")
    @GetMapping("/employee/update/password/reset/{employeeId}")
    @SaCheckPermission("system:employee:password:reset")
    public ResponseDTO<String> resetPassword(@PathVariable Integer employeeId) {
        return employeeService.resetPassword(employeeId);
    }

    @Operation(summary = "查询员工-根据部门id @author 卓大")
    @GetMapping("/employee/getAllEmployeeByDepartmentId/{departmentId}")
    public ResponseDTO<List<EmployeeVO>> getAllEmployeeByDepartmentId(@PathVariable Long departmentId) {
        return employeeService.getAllEmployeeByDepartmentId(departmentId, Boolean.FALSE);
    }

    @Operation(summary = "查询所有员工 @author 卓大")
    @GetMapping("/employee/queryAll")
    public ResponseDTO<List<EmployeeVO>> queryAllEmployee(@RequestParam(value = "disabledFlag", required = false) Boolean disabledFlag) {
        return employeeService.queryAllEmployee(disabledFlag);
    }

}
