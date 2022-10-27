package net.lab1024.smartadmin.module.system.role.roleemployee;

import net.lab1024.smartadmin.common.anno.OperateLog;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.smartadmin.module.system.role.basic.domain.dto.RoleBatchDTO;
import net.lab1024.smartadmin.module.system.role.basic.domain.dto.RoleQueryDTO;
import net.lab1024.smartadmin.module.system.role.basic.domain.dto.RoleSelectedVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户角色管理路由
 *
 * @author listen
 * @date 2017/12/28 10:10
 */
@Api(tags = {SwaggerTagConst.Admin.MANAGER_ROLE_USER})
@OperateLog
@RestController
public class RoleEmployeeController {

    @Autowired
    private RoleEmployeeService roleEmployeeService;

    @ApiOperation(value = "获取角色成员-员工列表", notes = "获取角色成员-员工列表（分页）")
    @PostMapping("/role/listEmployee")
    public ResponseDTO<PageResultDTO<EmployeeVO>> listEmployeeByName(@Valid @RequestBody RoleQueryDTO queryDTO) {
        return roleEmployeeService.listEmployeeByName(queryDTO);
    }

    @ApiOperation(value = "根据角色id获取角色员工列表(无分页)", notes = "根据角色id获取角色成员-员工列表")
    @GetMapping("/role/listAllEmployee/{roleId}")
    public ResponseDTO<List<EmployeeVO>> listAllEmployeeRoleId(@PathVariable Long roleId) {
        return roleEmployeeService.getAllEmployeeByRoleId(roleId);
    }

    @ApiOperation(value = "从角色成员列表中移除员工", notes = "从角色成员列表中移除员工")
    @ApiImplicitParams({@ApiImplicitParam(name = "employeeId", value = "员工id", paramType = "query", required = true), @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "query",
        required = true)})
    @GetMapping("/role/removeEmployee")
    public ResponseDTO<String> removeEmployee(Long employeeId, Long roleId) {
        return roleEmployeeService.removeEmployeeRole(employeeId, roleId);
    }

    @ApiOperation(value = "从角色成员列表中批量移除员工", notes = "从角色成员列表中批量移除员工")
    @PostMapping("/role/removeEmployeeList")
    public ResponseDTO<String> removeEmployeeList(@Valid @RequestBody RoleBatchDTO removeDTO) {
        return roleEmployeeService.batchRemoveEmployeeRole(removeDTO);
    }

    @ApiOperation(value = "角色成员列表中批量添加员工", notes = "角色成员列表中批量添加员工")
    @PostMapping("/role/addEmployeeList")
    public ResponseDTO<String> addEmployeeList(@Valid @RequestBody RoleBatchDTO addDTO) {
        return roleEmployeeService.batchAddEmployeeRole(addDTO);
    }

    @ApiOperation(value = "通过员工id获取所有角色以及员工具有的角色", notes = "通过员工id获取所有角色以及员工具有的角色")
    @GetMapping("/role/getRoles/{employeeId}")
    @ApiImplicitParams({@ApiImplicitParam(name = "employeeId", value = "员工id", paramType = "path", required = true)})
    public ResponseDTO<List<RoleSelectedVO>> getRoleByEmployeeId(@PathVariable Long employeeId) {
        return roleEmployeeService.getRolesByEmployeeId(employeeId);
    }
}
