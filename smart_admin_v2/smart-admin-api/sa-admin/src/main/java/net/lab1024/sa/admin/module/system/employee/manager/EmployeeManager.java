package net.lab1024.sa.admin.module.system.employee.manager;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleEmployeeEntity;
import net.lab1024.sa.admin.module.system.role.manager.RoleEmployeeManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工 manager
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021-12-29 21:52:46
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Service
public class EmployeeManager extends ServiceImpl<EmployeeDao, EmployeeEntity> {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private RoleEmployeeManager roleEmployeeManager;

    /**
     * 保存员工
     *
     * @param employee
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveEmployee(EmployeeEntity employee, List<Long> roleIdList) {
        // 保存员工 获得id
        employeeDao.insert(employee);

        if (CollectionUtils.isNotEmpty(roleIdList)) {
            List<RoleEmployeeEntity> roleEmployeeList = roleIdList.stream().map(e -> new RoleEmployeeEntity(e, employee.getEmployeeId())).collect(Collectors.toList());
            roleEmployeeManager.saveBatch(roleEmployeeList);
        }
    }

    /**
     * 更新员工
     *
     * @param employee
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateEmployee(EmployeeEntity employee, List<Long> roleIdList) {
        // 保存员工 获得id
        employeeDao.updateById(employee);

        if (CollectionUtils.isNotEmpty(roleIdList)) {
            List<RoleEmployeeEntity> roleEmployeeList = roleIdList.stream().map(e -> new RoleEmployeeEntity(e, employee.getEmployeeId())).collect(Collectors.toList());
            this.updateEmployeeRole(employee.getEmployeeId(), roleEmployeeList);
        }
    }

    /**
     * 更新员工角色
     *
     * @param employeeId
     * @param roleEmployeeList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateEmployeeRole(Long employeeId, List<RoleEmployeeEntity> roleEmployeeList) {
        roleEmployeeManager.getBaseMapper().deleteByEmployeeId(employeeId);

        if (CollectionUtils.isNotEmpty(roleEmployeeList)) {
            roleEmployeeManager.saveBatch(roleEmployeeList);
        }
    }

}
