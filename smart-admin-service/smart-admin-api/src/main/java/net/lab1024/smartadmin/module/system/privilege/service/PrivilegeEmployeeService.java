package net.lab1024.smartadmin.module.system.privilege.service;

import net.lab1024.smartadmin.common.constant.JudgeEnum;
import net.lab1024.smartadmin.common.exception.SmartBusinessException;
import net.lab1024.smartadmin.module.system.employee.domain.dto.EmployeeDTO;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import net.lab1024.smartadmin.module.system.privilege.constant.PrivilegeTypeEnum;
import net.lab1024.smartadmin.module.system.privilege.dao.PrivilegeDao;
import net.lab1024.smartadmin.module.system.privilege.domain.entity.PrivilegeEntity;
import net.lab1024.smartadmin.module.system.role.roleemployee.RoleEmployeeDao;
import net.lab1024.smartadmin.module.system.role.roleprivilege.RolePrivilegeDao;
import net.lab1024.smartadmin.module.system.systemconfig.SystemConfigService;
import net.lab1024.smartadmin.module.system.systemconfig.constant.SystemConfigEnum;
import net.lab1024.smartadmin.module.system.systemconfig.domain.dto.SystemConfigDTO;
import net.lab1024.smartadmin.util.SmartStringUtil;
import com.google.common.collect.Lists;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * [ 后台员工权限缓存方法 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/3/28 0028 下午 14:07
 * @since JDK1.8
 */
@Service
public class PrivilegeEmployeeService {

    /**
     * 后台用户权限缓存 <id,<controllerName,methodName></>></>
     */
    private ConcurrentMap<Long, Map<String, List<String>>> employeePrivileges = new ConcurrentLinkedHashMap.Builder<Long, Map<String, List<String>>>().maximumWeightedCapacity(1000).build();
    private ConcurrentMap<Long, List<PrivilegeEntity>> employeePrivilegeListMap = new ConcurrentLinkedHashMap.Builder<Long, List<PrivilegeEntity>>().maximumWeightedCapacity(1000).build();

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private RolePrivilegeDao rolePrivilegeDao;

    @Autowired
    private PrivilegeDao privilegeDao;

    /**
     * 移除某人缓存中的权限
     *
     * @param employeeId
     */
    public void removeCache(Long employeeId) {
        this.employeePrivileges.remove(employeeId);
    }

    /**
     * 检查某人是否有访问某个方法的权限
     *
     * @param requestTokenBO
     * @param controllerName
     * @param methodName
     * @return
     */
    public Boolean checkEmployeeHavePrivilege(RequestTokenBO requestTokenBO, String controllerName, String methodName) {
        if (StringUtils.isEmpty(controllerName) || StringUtils.isEmpty(methodName)) {
            return false;
        }
        Boolean isSuperman = requestTokenBO.getEmployeeBO().getIsSuperman();
        if (isSuperman) {
            return true;
        }
        Map<String, List<String>> privileges = this.getPrivileges(requestTokenBO.getRequestUserId());
        List<String> urlList = privileges.get(controllerName.toLowerCase());
        if (CollectionUtils.isEmpty(urlList)) {
            return false;
        }
        return urlList.contains(methodName);
    }

    public List<PrivilegeEntity> getEmployeeAllPrivilege(Long employeeId) {
        return employeePrivilegeListMap.get(employeeId);
    }

    /**
     * 判断是否为超级管理员
     *
     * @param employeeId
     * @return
     */
    public Boolean isSuperman(Long employeeId) {
        SystemConfigDTO systemConfig = systemConfigService.getCacheByKey(SystemConfigEnum.Key.EMPLOYEE_SUPERMAN);
        if (systemConfig == null) {
            throw new SmartBusinessException("缺少系统配置项[" + SystemConfigEnum.Key.EMPLOYEE_SUPERMAN.name() + "]");
        }

        List<Long> superManIdsList = SmartStringUtil.splitConverToLongList(systemConfig.getConfigValue(), ",");
        return superManIdsList.contains(employeeId);
    }

    /**
     * 根据员工ID 获取 权限信息
     *
     * @param employeeId
     * @return
     */
    public List<PrivilegeEntity> getPrivilegesByEmployeeId(Long employeeId) {
        List<PrivilegeEntity> privilegeEntities = null;
        // 如果是超管的话
        Boolean isSuperman = this.isSuperman(employeeId);
        if (isSuperman) {
            privilegeEntities = privilegeDao.selectAll();
        } else {
            privilegeEntities = loadPrivilegeFromDb(employeeId);
        }

        if (privilegeEntities == null) {
            privilegeEntities = Lists.newArrayList();
        }

        this.updateCachePrivilege(employeeId, privilegeEntities);
        return privilegeEntities;
    }

    /**
     * 获取某人所能访问的方法
     *
     * @param employeeId
     * @return
     */
    private Map<String, List<String>> getPrivileges(Long employeeId) {
        Map<String, List<String>> privileges = employeePrivileges.get(employeeId);
        if (privileges != null) {
            return privileges;
        }
        List<PrivilegeEntity> privilegeEntities = this.loadPrivilegeFromDb(employeeId);
        return updateCachePrivilege(employeeId, privilegeEntities);
    }

    private List<PrivilegeEntity> loadPrivilegeFromDb(Long employeeId) {
        List<Long> roleIdList = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        List<PrivilegeEntity> privilegeEntities = rolePrivilegeDao.listByRoleIds(roleIdList, JudgeEnum.YES.getValue());
        if (privilegeEntities != null) {
            return privilegeEntities;
        }
        return Collections.emptyList();
    }

    public Map<String, List<String>> updateCachePrivilege(Long employeeId, List<PrivilegeEntity> privilegeEntities) {
        employeePrivilegeListMap.put(employeeId, privilegeEntities);
        List<String> privilegeList = new ArrayList<>();
        Map<String, List<String>> privilegeMap = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(privilegeEntities)) {
            List<List<String>> setList =
                    privilegeEntities.stream().filter(e -> e.getType().equals(PrivilegeTypeEnum.POINTS.getValue())).map(PrivilegeEntity::getUrl).collect(Collectors.toList()).stream().map(e -> SmartStringUtil.splitConvertToList(e, ",")).collect(Collectors.toList());
            setList.forEach(privilegeList::addAll);
        }
        privilegeList.forEach(item -> {
            List<String> path = SmartStringUtil.splitConvertToList(item, "\\.");
            String controllerName = path.get(0).toLowerCase();
            String methodName = path.get(1);
            List<String> methodNameList = privilegeMap.get(controllerName);
            if (null == methodNameList) {
                methodNameList = new ArrayList();
            }
            if (!methodNameList.contains(methodName)) {
                methodNameList.add(methodName);
            }
            privilegeMap.put(controllerName, methodNameList);
        });

        employeePrivileges.put(employeeId, privilegeMap);
        return privilegeMap;
    }

    public void updateOnlineEmployeePrivilegeByRoleId(Long roleId) {
        List<EmployeeDTO> roleEmployeeList = roleEmployeeDao.selectEmployeeByRoleId(roleId);
        List<Long> employeeIdList = roleEmployeeList.stream().map(e -> e.getId()).collect(Collectors.toList());

        for (Long empId : employeePrivileges.keySet()) {
            if (employeeIdList.contains(empId)) {
                getPrivilegesByEmployeeId(empId);
            }
        }
    }
}
