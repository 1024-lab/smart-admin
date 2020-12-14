package net.lab1024.smartadmin.module.system.employee;

import net.lab1024.smartadmin.common.anno.NoValidPrivilege;
import net.lab1024.smartadmin.common.anno.OperateLog;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.module.system.employee.domain.dto.*;
import net.lab1024.smartadmin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import net.lab1024.smartadmin.util.SmartRequestTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 员工管理
 *
 * @author lidoudou
 * @date 2017年12月19日上午11:34:52
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_USER})
@OperateLog
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee/query")
    @ApiOperation(value = "员工管理查询", notes = "员工管理查询 @author lidoudou")
    public ResponseDTO<PageResultDTO<EmployeeVO>> query(@RequestBody EmployeeQueryDTO query) {
        return employeeService.selectEmployeeList(query);
    }

    @GetMapping("/employee/get/all")
    @ApiOperation(value = "查询所有员工基本信息，用于选择框", notes = "查询所有员工基本信息，用于选择框")
    @NoValidPrivilege
    public ResponseDTO<List<EmployeeVO>> getAll() {
        return ResponseDTO.succData(employeeService.getAllEmployee());
    }

    @ApiOperation(value = "添加员工", notes = "@author yandanyang")
    @PostMapping("/employee/add")
    public ResponseDTO<String> addEmployee(@Valid @RequestBody EmployeeAddDTO emp) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        return employeeService.addEmployee(emp, requestToken);
    }

    @ApiOperation(value = "禁用单个员工", notes = "@author yandanyang")
    @GetMapping("/employee/updateStatus/{employeeId}/{status}")
    public ResponseDTO<String> updateStatus(@PathVariable("employeeId") Long employeeId, @PathVariable("status") Integer status) {
        return employeeService.updateStatus(employeeId, status);
    }

    @ApiOperation(value = "批量禁用", notes = "@author yandanyang")
    @PostMapping("/employee/batchUpdateStatus")
    public ResponseDTO<String> batchUpdateStatus(@Valid @RequestBody EmployeeBatchUpdateStatusDTO batchUpdateStatusDTO) {
        return employeeService.batchUpdateStatus(batchUpdateStatusDTO);
    }

    @ApiOperation(value = "更新员工信息", notes = "@author yandanyang")
    @PostMapping("/employee/update")
    public ResponseDTO<String> updateEmployee(@Valid @RequestBody EmployeeUpdateDTO employeeUpdateDto) {
        return employeeService.updateEmployee(employeeUpdateDto);
    }

    @ApiOperation(value = "删除员工信息", notes = "@author yandanyang")
    @PostMapping("/employee/delete/{employeeId}")
    public ResponseDTO<String> deleteEmployeeById(@PathVariable("employeeId") Long employeeId) {
        return employeeService.deleteEmployeeById(employeeId);
    }

    @ApiOperation(value = "单个员工角色授权", notes = "@author yandanyang")
    @PostMapping("/employee/updateRoles")
    public ResponseDTO<String> updateRoles(@Valid @RequestBody EmployeeUpdateRolesDTO updateRolesDTO) {
        return employeeService.updateRoles(updateRolesDTO);
    }

    @ApiOperation(value = "修改密码", notes = "@author yandanyang")
    @PostMapping("/employee/updatePwd")
    public ResponseDTO<String> updatePwd(@Valid @RequestBody EmployeeUpdatePwdDTO updatePwdDTO) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        return employeeService.updatePwd(updatePwdDTO, requestToken);
    }

    @ApiOperation(value = "通过部门id获取当前部门的人员&没有部门的人", notes = "@author yandanyang")
    @GetMapping("/employee/listEmployeeByDeptId/{deptId}")
    public ResponseDTO<List<EmployeeVO>> listEmployeeByDeptId(@PathVariable Long deptId) {
        return employeeService.getEmployeeByDeptId(deptId);
    }

    @ApiOperation(value = "员工重置密码", notes = "@author lizongliang")
    @GetMapping("/employee/resetPasswd/{employeeId}")
    public ResponseDTO resetPasswd(@PathVariable("employeeId") Integer employeeId) {
        return employeeService.resetPasswd(employeeId);
    }

}
