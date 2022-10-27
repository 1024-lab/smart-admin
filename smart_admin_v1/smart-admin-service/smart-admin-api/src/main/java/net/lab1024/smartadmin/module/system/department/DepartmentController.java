package net.lab1024.smartadmin.module.system.department;

import net.lab1024.smartadmin.common.anno.OperateLog;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.system.department.domain.dto.DepartmentCreateDTO;
import net.lab1024.smartadmin.module.system.department.domain.dto.DepartmentUpdateDTO;
import net.lab1024.smartadmin.module.system.department.domain.dto.DepartmentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 部门管理路由器
 *
 * @author listen
 * @date 2017/12/19 14:29
 */
@Api(tags = {SwaggerTagConst.Admin.MANAGER_DEPARTMENT})
@OperateLog
@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询部门树形列表", notes = "查询部门列表")
    @GetMapping("/department/list")
    public ResponseDTO<List<DepartmentVO>> listDepartment() {
        return departmentService.listDepartment();
    }

    @ApiOperation(value = "查询部门及员工列表", notes = "查询部门及员工列表")
    @GetMapping("/department/listEmployee")
    public ResponseDTO<List<DepartmentVO>> listDepartmentEmployee() {
        return departmentService.listAllDepartmentEmployee(null);
    }

    @ApiOperation(value = "根据部门名称查询部门及员工列表", notes = "根据部门名称查询部门及员工列表")
    @GetMapping("/department/listEmployeeByDepartmentName")
    public ResponseDTO<List<DepartmentVO>> listDepartmentEmployee(String departmentName) {
        return departmentService.listAllDepartmentEmployee(departmentName);
    }

    @ApiOperation(value = "添加部门", notes = "添加部门")
    @PostMapping("/department/add")
    public ResponseDTO<String> addDepartment(@Valid @RequestBody DepartmentCreateDTO departmentCreateDTO) {
        return departmentService.addDepartment(departmentCreateDTO);
    }

    @ApiOperation(value = "更新部门信息", notes = "更新部门信息")
    @PostMapping("/department/update")
    public ResponseDTO<String> updateDepartment(@Valid @RequestBody DepartmentUpdateDTO departmentUpdateDTO) {
        return departmentService.updateDepartment(departmentUpdateDTO);
    }

    @ApiOperation(value = "删除部门", notes = "删除部门")
    @PostMapping("/department/delete/{deptId}")
    public ResponseDTO<String> delDepartment(@PathVariable Long deptId) {
        return departmentService.delDepartment(deptId);
    }

    @ApiOperation(value = "获取部门信息", notes = "获取部门")
    @GetMapping("/department/query/{deptId}")
    public ResponseDTO<DepartmentVO> getDepartment(@PathVariable Long deptId) {
        return departmentService.getDepartmentById(deptId);
    }

    @ApiOperation(value = "查询部门列表", notes = "查询部门列表")
    @GetMapping("/department/listAll")
    public ResponseDTO<List<DepartmentVO>> listAll() {
        return departmentService.listAll();
    }


    @ApiOperation(value = "上下移动")
    @GetMapping("/department/upOrDown/{deptId}/{swapId}")
    public ResponseDTO<String> upOrDown(@PathVariable Long deptId, @PathVariable Long swapId) {
        return departmentService.upOrDown(deptId, swapId);
    }

    @ApiOperation(value = "升级")
    @GetMapping("/department/upgrade/{deptId}")
    public ResponseDTO<String> upgrade(@PathVariable Long deptId) {
        return departmentService.upgrade(deptId);
    }

    @ApiOperation(value = "降级")
    @GetMapping("/department/downgrade/{deptId}/{preId}")
    public ResponseDTO<String> downgrade(@PathVariable Long deptId, @PathVariable Long preId) {
        return departmentService.downgrade(deptId, preId);
    }


}
