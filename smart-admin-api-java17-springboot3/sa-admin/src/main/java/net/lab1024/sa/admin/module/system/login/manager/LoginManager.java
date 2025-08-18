package net.lab1024.sa.admin.module.system.login.manager;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminCacheConst;
import net.lab1024.sa.admin.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.sa.admin.module.system.department.service.DepartmentService;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.sa.admin.module.system.employee.service.EmployeeService;
import net.lab1024.sa.admin.module.system.login.domain.RequestEmployee;
import net.lab1024.sa.admin.module.system.menu.domain.vo.MenuVO;
import net.lab1024.sa.admin.module.system.role.domain.vo.RoleVO;
import net.lab1024.sa.admin.module.system.role.service.RoleEmployeeService;
import net.lab1024.sa.admin.module.system.role.service.RoleMenuService;
import net.lab1024.sa.base.common.constant.StringConst;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.UserPermission;
import net.lab1024.sa.base.common.enumeration.UserTypeEnum;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.module.support.file.service.IFileStorageService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登录Manager
 *
 * @Author 1024创新实验室: 卓大
 * @Date 2025-05-03 22:56:34
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class LoginManager {

    @Resource
    private DepartmentService departmentService;

    @Resource
    private IFileStorageService fileStorageService;

    @Resource
    private EmployeeService employeeService;

    @Resource
    private RoleEmployeeService roleEmployeeService;

    @Resource
    private RoleMenuService roleMenuService;


    /**
     * 获取请求用户信息
     */
    @Cacheable(AdminCacheConst.Login.REQUEST_EMPLOYEE)
    public RequestEmployee getRequestEmployee(Long requestEmployeeId ) {
        if (requestEmployeeId == null) {
            return null;
        }
        // 员工基本信息
        EmployeeEntity employeeEntity = employeeService.getById(requestEmployeeId);
        if (employeeEntity == null) {
            return null;
        }

        return this.loadLoginInfo(employeeEntity);
    }

    /**
     * 获取登录的用户信息
     */
    @CachePut(value = AdminCacheConst.Login.REQUEST_EMPLOYEE, key = "#employeeEntity.employeeId")
    public RequestEmployee loadLoginInfo(EmployeeEntity employeeEntity) {
        // 基础信息
        RequestEmployee requestEmployee = SmartBeanUtil.copy(employeeEntity, RequestEmployee.class);
        requestEmployee.setUserType(UserTypeEnum.ADMIN_EMPLOYEE);

        // 部门信息
        DepartmentVO department = departmentService.getDepartmentById(employeeEntity.getDepartmentId());
        requestEmployee.setDepartmentName(null == department ? StringConst.EMPTY : department.getDepartmentName());

        // 头像信息
        String avatar = employeeEntity.getAvatar();
        if (StringUtils.isNotBlank(avatar)) {
            ResponseDTO<String> getFileUrl = fileStorageService.getFileUrl(avatar);
            if (BooleanUtils.isTrue(getFileUrl.getOk())) {
                requestEmployee.setAvatar(getFileUrl.getData());
            }
        }
        return requestEmployee;
    }


    /**
     * 获取用户的权限（包含 角色列表、权限列表）
     */
    @Cacheable(AdminCacheConst.Login.USER_PERMISSION)
    public UserPermission getUserPermission(Long employeeId) {
        if(null == employeeId){
            return null;
        }

        return this.loadUserPermission(employeeId);
    }

    /**
     * 获取用户的权限（包含 角色列表、权限列表）
     */
    @CachePut(AdminCacheConst.Login.USER_PERMISSION)
    public UserPermission loadUserPermission(Long employeeId) {
        UserPermission userPermission = new UserPermission();
        userPermission.setPermissionList(new ArrayList<>());
        userPermission.setRoleList(new ArrayList<>());

        // 角色列表
        List<RoleVO> roleList = roleEmployeeService.getRoleIdList(employeeId);
        userPermission.getRoleList().addAll(roleList.stream().map(RoleVO::getRoleCode).collect(Collectors.toSet()));

        // 前端菜单和功能点清单
        EmployeeEntity employeeEntity = employeeService.getById(employeeId);

        List<MenuVO> menuAndPointsList = roleMenuService.getMenuList(roleList.stream().map(RoleVO::getRoleId).collect(Collectors.toList()), employeeEntity.getAdministratorFlag());

        // 权限列表
        HashSet<String> permissionSet = new HashSet<>();
        for (MenuVO menu : menuAndPointsList) {
            if (menu.getPermsType() == null) {
                continue;
            }

            String perms = menu.getApiPerms();
            if (StringUtils.isEmpty(perms)) {
                continue;
            }
            //接口权限
            String[] split = perms.split(",");
            permissionSet.addAll(Arrays.asList(split));
        }
        userPermission.getPermissionList().addAll(permissionSet);

        return userPermission;
    }


    /**
     * 清除用户权限
     */
    @CacheEvict(value = AdminCacheConst.Login.USER_PERMISSION)
    public void clearUserPermission(Long employeeId) {

    }

    /**
     * 清除用户登录信息
     */
    @CacheEvict(value = AdminCacheConst.Login.REQUEST_EMPLOYEE)
    public void clearUserLoginInfo(Long employeeId) {

    }


}
