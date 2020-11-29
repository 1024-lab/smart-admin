package net.lab1024.smartadmin.module.system.datascope.service;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.module.system.datascope.DataScopeRoleDao;
import net.lab1024.smartadmin.module.system.datascope.constant.DataScopeTypeEnum;
import net.lab1024.smartadmin.module.system.datascope.constant.DataScopeViewTypeEnum;
import net.lab1024.smartadmin.module.system.datascope.domain.entity.DataScopeRoleEntity;
import net.lab1024.smartadmin.module.system.department.DepartmentTreeService;
import net.lab1024.smartadmin.module.system.employee.EmployeeDao;
import net.lab1024.smartadmin.module.system.employee.domain.dto.EmployeeDTO;
import net.lab1024.smartadmin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.smartadmin.module.system.privilege.service.PrivilegeEmployeeService;
import net.lab1024.smartadmin.module.system.role.roleemployee.RoleEmployeeDao;
import net.lab1024.smartadmin.util.SmartBaseEnumUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/28 0028 下午 15:56
 * @since JDK1.8
 */
@Service
public class DataScopeViewService {

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private DataScopeRoleDao dataScopeRoleDao;

    @Autowired
    private DepartmentTreeService departmentTreeService;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private PrivilegeEmployeeService privilegeEmployeeService;

    /**
     * 获取某人可以查看的所有人员信息
     *
     * @param dataScopeTypeEnum
     * @param employeeId
     * @return
     */
    public List<Long> getCanViewEmployeeId(DataScopeTypeEnum dataScopeTypeEnum, Long employeeId) {
        DataScopeViewTypeEnum viewType = this.getEmployeeDataScopeViewType(dataScopeTypeEnum, employeeId);
        if (DataScopeViewTypeEnum.ME == viewType) {
            return this.getMeEmployeeIdList(employeeId);
        }
        if (DataScopeViewTypeEnum.DEPARTMENT == viewType) {
            return this.getDepartmentEmployeeIdList(employeeId);
        }
        if (DataScopeViewTypeEnum.DEPARTMENT_AND_SUB == viewType) {
            return this.getDepartmentAndSubEmployeeIdList(employeeId);
        }
        return Lists.newArrayList();
    }

    /**
     * 获取某人可以查看的所有部门信息
     *
     * @param dataScopeTypeEnum
     * @param employeeId
     * @return
     */
    public List<Long> getCanViewDepartmentId(DataScopeTypeEnum dataScopeTypeEnum, Long employeeId) {
        DataScopeViewTypeEnum viewType = this.getEmployeeDataScopeViewType(dataScopeTypeEnum, employeeId);
        if (DataScopeViewTypeEnum.ME == viewType) {
            return this.getMeDepartmentIdList(employeeId);
        }
        if (DataScopeViewTypeEnum.DEPARTMENT == viewType) {
            return this.getMeDepartmentIdList(employeeId);
        }
        if (DataScopeViewTypeEnum.DEPARTMENT_AND_SUB == viewType) {
            return this.getDepartmentAndSubIdList(employeeId);
        }
        return Lists.newArrayList();
    }

    private List<Long> getMeDepartmentIdList(Long employeeId) {
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        return Lists.newArrayList(employeeEntity.getDepartmentId());
    }

    private List<Long> getDepartmentAndSubIdList(Long employeeId) {
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        List<Long> allDepartmentIds = Lists.newArrayList();
        departmentTreeService.buildIdList(employeeEntity.getDepartmentId(), allDepartmentIds);
        return allDepartmentIds;
    }

    /**
     * 根据员工id 获取各数据范围最大的可见范围 map<dataScopeType,viewType></>
     *
     * @param employeeId
     * @return
     */
    public DataScopeViewTypeEnum getEmployeeDataScopeViewType(DataScopeTypeEnum dataScopeTypeEnum, Long employeeId) {
        if (employeeId == null) {
            return DataScopeViewTypeEnum.ME;
        }

        if (privilegeEmployeeService.isSuperman(employeeId)) {
            return DataScopeViewTypeEnum.ALL;
        }
        List<Long> roleIdList = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        //未设置角色 默认本人
        if (CollectionUtils.isEmpty(roleIdList)) {
            return DataScopeViewTypeEnum.ME;
        }
        //未设置角色数据范围 默认本人
        List<DataScopeRoleEntity> dataScopeRoleList = dataScopeRoleDao.listByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(dataScopeRoleList)) {
            return DataScopeViewTypeEnum.ME;
        }
        Map<Integer, List<DataScopeRoleEntity>> listMap = dataScopeRoleList.stream().collect(Collectors.groupingBy(DataScopeRoleEntity::getDataScopeType));
        List<DataScopeRoleEntity> viewLevelList = listMap.get(dataScopeTypeEnum.getValue());
        DataScopeRoleEntity maxLevel = viewLevelList.stream().max(Comparator.comparing(e -> SmartBaseEnumUtil.getEnumByValue(e.getViewType(), DataScopeViewTypeEnum.class).getLevel())).get();
        return SmartBaseEnumUtil.getEnumByValue(maxLevel.getViewType(), DataScopeViewTypeEnum.class);
    }

    /**
     * 获取本人相关 可查看员工id
     *
     * @param employeeId
     * @return
     */
    private List<Long> getMeEmployeeIdList(Long employeeId) {
        return Lists.newArrayList(employeeId);
    }

    /**
     * 获取本部门相关 可查看员工id
     *
     * @param employeeId
     * @return
     */
    private List<Long> getDepartmentEmployeeIdList(Long employeeId) {
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        List<EmployeeVO> employeeList = employeeDao.getEmployeeIdByDeptId(employeeEntity.getDepartmentId());
        List<Long> employeeIdList = employeeList.stream().map(e -> e.getId()).collect(Collectors.toList());
        return employeeIdList;
    }

    /**
     * 获取本部门及下属子部门相关 可查看员工id
     *
     * @param employeeId
     * @return
     */
    private List<Long> getDepartmentAndSubEmployeeIdList(Long employeeId) {
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        List<Long> allDepartmentIds = Lists.newArrayList();
        departmentTreeService.buildIdList(employeeEntity.getDepartmentId(), allDepartmentIds);
        List<EmployeeDTO> employeeList = employeeDao.getEmployeeIdByDeptIds(allDepartmentIds);
        List<Long> employeeIdList = employeeList.stream().map(e -> e.getId()).collect(Collectors.toList());
        return employeeIdList;
    }

}
