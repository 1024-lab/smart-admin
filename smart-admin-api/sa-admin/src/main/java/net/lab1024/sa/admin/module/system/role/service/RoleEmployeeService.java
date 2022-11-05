package net.lab1024.sa.admin.module.system.role.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.system.role.dao.RoleDao;
import net.lab1024.sa.admin.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleEntity;
import net.lab1024.sa.admin.module.system.role.domain.form.RoleEmployeeQueryForm;
import net.lab1024.sa.admin.module.system.role.domain.form.RoleEmployeeUpdateForm;
import net.lab1024.sa.admin.module.system.role.domain.vo.RoleSelectedVO;
import net.lab1024.sa.common.common.code.UserErrorCode;
import net.lab1024.sa.common.common.constant.StringConst;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.common.util.SmartBeanUtil;
import net.lab1024.sa.common.common.util.SmartPageUtil;
import net.lab1024.sa.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.sa.admin.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleEmployeeEntity;
import net.lab1024.sa.admin.module.system.role.manager.RoleEmployeeManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色-员工
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2021-10-22 23:17:47
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Service
public class RoleEmployeeService {

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private RoleEmployeeManager roleEmployeeManager;

    /**
     * 通过角色id，分页获取成员员工列表
     *
     * @param roleEmployeeQueryForm
     * @return
     */
    public ResponseDTO<PageResult<EmployeeVO>> queryEmployee(RoleEmployeeQueryForm roleEmployeeQueryForm) {
        Page page = SmartPageUtil.convert2PageQuery(roleEmployeeQueryForm);
        List<EmployeeVO> employeeDTOS = roleEmployeeDao.selectRoleEmployeeByName(page, roleEmployeeQueryForm)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<Long> departmentIdList = employeeDTOS.stream().filter(e -> e != null && e.getDepartmentId() != null).map(EmployeeVO::getDepartmentId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(departmentIdList)) {
            List<DepartmentEntity> departmentEntities = departmentDao.selectBatchIds(departmentIdList);
            Map<Long, String> departmentIdNameMap = departmentEntities.stream().collect(Collectors.toMap(DepartmentEntity::getDepartmentId, DepartmentEntity::getName));
            employeeDTOS.forEach(e -> {
                e.setDepartmentName(departmentIdNameMap.getOrDefault(e.getDepartmentId(), StringConst.EMPTY));
            });
        }
        PageResult<EmployeeVO> PageResult = SmartPageUtil.convert2PageResult(page, employeeDTOS, EmployeeVO.class);
        return ResponseDTO.ok(PageResult);
    }

    public List<EmployeeVO> getAllEmployeeByRoleId(Long roleId) {
        return roleEmployeeDao.selectEmployeeByRoleId(roleId);
    }

    /**
     * 移除员工角色
     *
     * @param employeeId
     * @param roleId
     * @return ResponseDTO<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> removeRoleEmployee(Long employeeId, Long roleId) {
        if (null == employeeId || null == roleId) {
            return ResponseDTO.userErrorParam();
        }
        roleEmployeeDao.deleteByEmployeeIdRoleId(employeeId, roleId);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除角色的成员员工
     *
     * @param roleEmployeeUpdateForm
     * @return ResponseDTO<String>
     */
    public ResponseDTO<String> batchRemoveRoleEmployee(RoleEmployeeUpdateForm roleEmployeeUpdateForm) {
        roleEmployeeDao.batchDeleteEmployeeRole(roleEmployeeUpdateForm.getRoleId(), roleEmployeeUpdateForm.getEmployeeIdList());
        return ResponseDTO.ok();
    }

    /**
     * 批量添加角色的成员员工
     *
     * @param roleEmployeeUpdateForm
     * @return
     */
    public ResponseDTO<String> batchAddRoleEmployee(RoleEmployeeUpdateForm roleEmployeeUpdateForm) {
        Long roleId = roleEmployeeUpdateForm.getRoleId();
        List<Long> employeeIdList = roleEmployeeUpdateForm.getEmployeeIdList();
        // 保存新的角色员工
        List<RoleEmployeeEntity> roleEmployeeList = null;
        if (CollectionUtils.isNotEmpty(employeeIdList)) {
            roleEmployeeList = employeeIdList.stream()
                    .map(employeeId -> new RoleEmployeeEntity(roleId, employeeId))
                    .collect(Collectors.toList());
        }
        // 保存数据
        roleEmployeeManager.saveRoleEmployee(roleId, roleEmployeeList);
        return ResponseDTO.ok();
    }

    /**
     * 通过员工id获取员工角色
     *
     * @param employeeId
     * @return
     */
    public List<RoleSelectedVO> getRoleInfoListByEmployeeId(Long employeeId) {
        List<Long> roleIds = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        List<RoleEntity> roleList = roleDao.selectList(null);
        List<RoleSelectedVO> result = SmartBeanUtil.copyList(roleList, RoleSelectedVO.class);
        result.stream().forEach(item -> item.setSelected(roleIds.contains(item.getRoleId())));
        return result;
    }

    /**
     * 根据员工id 查询角色id集合
     *
     * @param employeeId
     * @return
     */
    public List<Long> getRoleIdList(Long employeeId) {
        return roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
    }


}
