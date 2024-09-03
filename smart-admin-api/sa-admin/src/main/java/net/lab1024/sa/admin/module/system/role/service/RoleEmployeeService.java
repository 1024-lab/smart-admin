package net.lab1024.sa.admin.module.system.role.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.sa.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.sa.admin.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.sa.admin.module.system.role.dao.RoleDao;
import net.lab1024.sa.admin.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleEmployeeEntity;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleEntity;
import net.lab1024.sa.admin.module.system.role.domain.form.RoleEmployeeQueryForm;
import net.lab1024.sa.admin.module.system.role.domain.form.RoleEmployeeUpdateForm;
import net.lab1024.sa.admin.module.system.role.domain.vo.RoleSelectedVO;
import net.lab1024.sa.admin.module.system.role.domain.vo.RoleVO;
import net.lab1024.sa.admin.module.system.role.manager.RoleEmployeeManager;
import net.lab1024.sa.base.common.constant.StringConst;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色-员工
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2021-10-22 23:17:47
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class RoleEmployeeService {

    @Resource
    private RoleEmployeeDao roleEmployeeDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private DepartmentDao departmentDao;
    @Resource
    private RoleEmployeeManager roleEmployeeManager;


    /**
     * 批量插入
     *
     */
    public void batchInsert(List<RoleEmployeeEntity> roleEmployeeList) {
        roleEmployeeManager.saveBatch(roleEmployeeList);
    }

    /**
     * 通过角色id，分页获取成员员工列表
     *
     */
    public ResponseDTO<PageResult<EmployeeVO>> queryEmployee(RoleEmployeeQueryForm roleEmployeeQueryForm) {
        Page page = SmartPageUtil.convert2PageQuery(roleEmployeeQueryForm);
        List<EmployeeVO> employeeList = roleEmployeeDao.selectRoleEmployeeByName(page, roleEmployeeQueryForm)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<Long> departmentIdList = employeeList.stream().filter(e -> e != null && e.getDepartmentId() != null).map(EmployeeVO::getDepartmentId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(departmentIdList)) {
            List<DepartmentEntity> departmentEntities = departmentDao.selectBatchIds(departmentIdList);
            Map<Long, String> departmentIdNameMap = departmentEntities.stream().collect(Collectors.toMap(DepartmentEntity::getDepartmentId, DepartmentEntity::getName));
            employeeList.forEach(e -> {
                e.setDepartmentName(departmentIdNameMap.getOrDefault(e.getDepartmentId(), StringConst.EMPTY));
            });
        }
        PageResult<EmployeeVO> pageResult = SmartPageUtil.convert2PageResult(page, employeeList, EmployeeVO.class);
        return ResponseDTO.ok(pageResult);
    }

    public List<EmployeeVO> getAllEmployeeByRoleId(Long roleId) {
        return roleEmployeeDao.selectEmployeeByRoleId(roleId);
    }

    /**
     * 移除员工角色
     *
     */
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
     */
    public ResponseDTO<String> batchRemoveRoleEmployee(RoleEmployeeUpdateForm roleEmployeeUpdateForm) {
        roleEmployeeDao.batchDeleteEmployeeRole(roleEmployeeUpdateForm.getRoleId(), roleEmployeeUpdateForm.getEmployeeIdList());
        return ResponseDTO.ok();
    }

    /**
     * 批量添加角色的成员员工
     *
     */
    public ResponseDTO<String> batchAddRoleEmployee(RoleEmployeeUpdateForm roleEmployeeUpdateForm) {
        Long roleId = roleEmployeeUpdateForm.getRoleId();

        // 已选择的员工id列表
        Set<Long> selectedEmployeeIdList = roleEmployeeUpdateForm.getEmployeeIdList();
        // 数据库里已有的员工id列表
        Set<Long> dbEmployeeIdList = roleEmployeeDao.selectEmployeeIdByRoleIdList(Lists.newArrayList(roleId));
        // 从已选择的员工id列表里 过滤数据库里不存在的 即需要添加的员工 id
        Set<Long> addEmployeeIdList = selectedEmployeeIdList.stream().filter(id -> !dbEmployeeIdList.contains(id)).collect(Collectors.toSet());

        // 添加角色员工
        if (CollectionUtils.isNotEmpty(addEmployeeIdList)) {
            List<RoleEmployeeEntity> roleEmployeeList = addEmployeeIdList.stream()
                    .map(employeeId -> new RoleEmployeeEntity(roleId, employeeId))
                    .collect(Collectors.toList());
            roleEmployeeManager.saveBatch(roleEmployeeList);
        }
        return ResponseDTO.ok();
    }

    /**
     * 通过员工id获取员工角色
     *
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
     */
    public List<RoleVO> getRoleIdList(Long employeeId) {
        return roleEmployeeDao.selectRoleByEmployeeId(employeeId);
    }


}
