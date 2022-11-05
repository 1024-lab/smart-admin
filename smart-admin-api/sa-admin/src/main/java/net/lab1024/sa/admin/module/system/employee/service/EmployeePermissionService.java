package net.lab1024.sa.admin.module.system.employee.service;

import net.lab1024.sa.admin.module.system.menu.constant.MenuPermsTypeEnum;
import net.lab1024.sa.admin.module.system.menu.domain.vo.MenuVO;
import net.lab1024.sa.admin.module.system.role.service.RoleEmployeeService;
import net.lab1024.sa.admin.module.system.role.service.RoleMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 员工权限校验
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021-12-29 21:52:46
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Service
public class EmployeePermissionService {

    @Autowired
    private RoleEmployeeService roleEmployeeService;

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 构建权限集合
     *
     * @param menuAndPointsList
     */
    public Set<? extends GrantedAuthority> buildAuthorities(List<MenuVO> menuAndPointsList) {
        HashSet<String> permissionList = new HashSet<>();
        for (MenuVO menu : menuAndPointsList) {
            if(menu.getPermsType() == null){
                continue;
            }

            String perms = null;
            if(menu.getPermsType().equals(MenuPermsTypeEnum.SPRING_SECURITY.getValue())){
                perms = menu.getWebPerms();
            }else{
                perms = menu.getApiPerms();
            }

            if (StringUtils.isEmpty(perms)) {
                continue;
            }
            //接口权限
            String[] split = perms.split(",");
            for (String perm : split) {
                permissionList.add(perm);
            }
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(permissionList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        return authorities;
    }

    /**
     * 查询用户拥有的前端菜单项 用于登陆返回 前端动态路由配置
     *
     * @param employeeId
     * @return
     */
    public List<MenuVO> getEmployeeMenuAndPointsList(Long employeeId, Boolean administratorFlag) {
        List<Long> roleIdList = roleEmployeeService.getRoleIdList(employeeId);
        return roleMenuService.getMenuList(roleIdList, administratorFlag);
    }

}
