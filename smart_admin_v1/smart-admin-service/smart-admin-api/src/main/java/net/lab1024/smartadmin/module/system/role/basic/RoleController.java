package net.lab1024.smartadmin.module.system.role.basic;
import net.lab1024.smartadmin.common.anno.OperateLog;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.module.system.role.basic.domain.dto.RoleAddDTO;
import net.lab1024.smartadmin.module.system.role.basic.domain.dto.RoleUpdateDTO;
import net.lab1024.smartadmin.module.system.role.basic.domain.dto.RoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色管理路由
 *
 * @author listen
 * @date 2017/12/28 10:10
 */
@Api(tags = {SwaggerTagConst.Admin.MANAGER_ROLE})
@OperateLog
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "添加角色", notes = "添加角色")
    @PostMapping("/role/add")
    public ResponseDTO addRole(@Valid @RequestBody RoleAddDTO roleAddDTO) {
        return roleService.addRole(roleAddDTO);
    }

    @ApiOperation(value = "删除角色", notes = "根据id删除角色")
    @GetMapping("/role/delete/{roleId}")
    public ResponseDTO<String> deleteRole(@PathVariable("roleId") Long roleId) {
        return roleService.deleteRole(roleId);
    }

    @ApiOperation(value = "更新角色", notes = "更新角色")
    @PostMapping("/role/update")
    public ResponseDTO<String> updateRole(@Valid @RequestBody RoleUpdateDTO roleUpdateDTO) {
        return roleService.updateRole(roleUpdateDTO);
    }

    @ApiOperation(value = "获取角色数据", notes = "根据id获取角色数据")
    @GetMapping("/role/get/{roleId}")
    public ResponseDTO<RoleVO> getRole(@PathVariable("roleId") Long roleId) {
        return roleService.getRoleById(roleId);
    }

    @ApiOperation(value = "获取所有角色", notes = "获取所有角色数据")
    @GetMapping("/role/getAll")
    public ResponseDTO<List<RoleVO>> getAllRole() {
        return roleService.getAllRole();
    }

}
