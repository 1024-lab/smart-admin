package com.gangquan360.smartadmin.module.role.roleemployee;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gangquan360.smartadmin.common.domain.PageResultDTO;
import com.gangquan360.smartadmin.common.domain.ResponseDTO;
import com.gangquan360.smartadmin.module.department.DepartmentDao;
import com.gangquan360.smartadmin.module.department.domain.entity.DepartmentEntity;
import com.gangquan360.smartadmin.module.employee.domain.dto.EmployeeDTO;
import com.gangquan360.smartadmin.module.employee.domain.vo.EmployeeVO;
import com.gangquan360.smartadmin.module.role.basic.RoleDao;
import com.gangquan360.smartadmin.module.role.basic.RoleResponseCodeConst;
import com.gangquan360.smartadmin.module.role.basic.domain.dto.RoleBatchDTO;
import com.gangquan360.smartadmin.module.role.basic.domain.dto.RoleQueryDTO;
import com.gangquan360.smartadmin.module.role.basic.domain.dto.RoleSelectedVO;
import com.gangquan360.smartadmin.module.role.basic.domain.dto.RoleVO;
import com.gangquan360.smartadmin.module.role.roleemployee.domain.RoleEmployeeEntity;
import com.gangquan360.smartadmin.util.SmartBeanUtil;
import com.gangquan360.smartadmin.util.SmartPaginationUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色管理业务
 *
 * @author zzr
 * @date 2019/4/3
 */
@Service
public class RoleEmployeeService {

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 通过角色id，分页获取成员员工列表
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<EmployeeVO>> listEmployeeByName(RoleQueryDTO queryDTO) {
        Page page = SmartPaginationUtil.convert2PageQueryInfo(queryDTO);
        List<EmployeeDTO> employeeDTOS = roleEmployeeDao.selectEmployeeByNamePage(page, queryDTO);
        employeeDTOS.stream().filter(e -> e.getDepartmentId() != null).forEach(employeeDTO -> {
            DepartmentEntity departmentEntity = departmentDao.selectById(employeeDTO.getDepartmentId());
            employeeDTO.setDepartmentName(departmentEntity.getName());
        });
        PageResultDTO<EmployeeVO> pageResultDTO = SmartPaginationUtil.convert2PageInfoDTO(page, employeeDTOS, EmployeeVO.class);
        return ResponseDTO.succData(pageResultDTO);
    }

    public ResponseDTO<List<EmployeeVO>> getAllEmployeeByRoleId(Long roleId) {
        List<EmployeeDTO> employeeDTOS = roleEmployeeDao.selectEmployeeByRoleId(roleId);
        List<EmployeeVO> list = SmartBeanUtil.copyList(employeeDTOS, EmployeeVO.class);
        return ResponseDTO.succData(list);
    }

    /**
     * 移除员工角色
     *
     * @param employeeId
     * @param roleId
     * @return ResponseDTO<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> removeEmployeeRole(Long employeeId, Long roleId) {
        if (null == employeeId || null == roleId) {
            return ResponseDTO.wrap(RoleResponseCodeConst.ERROR_PARAM);
        }
        roleEmployeeDao.deleteByEmployeeIdRoleId(employeeId, roleId);
        return ResponseDTO.succ();
    }

    /**
     * 批量删除角色的成员员工
     *
     * @param removeDTO
     * @return ResponseDTO<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> batchRemoveEmployeeRole(RoleBatchDTO removeDTO) {
        List<Long> employeeIdList = removeDTO.getEmployeeIds();
        roleEmployeeDao.batchDeleteEmployeeRole(removeDTO.getRoleId(), employeeIdList);
        return ResponseDTO.succ();
    }

    /**
     * 批量添加角色的成员员工
     *
     * @param addDTO
     * @return ResponseDTO<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> batchAddEmployeeRole(RoleBatchDTO addDTO) {
        Long roleId = addDTO.getRoleId();
        List<Long> employeeIdList = addDTO.getEmployeeIds();
        roleEmployeeDao.deleteByRoleId(roleId);
        List<RoleEmployeeEntity> roleRelationEntities = Lists.newArrayList();
        RoleEmployeeEntity employeeRoleRelationEntity;
        for (Long employeeId : employeeIdList) {
            employeeRoleRelationEntity = new RoleEmployeeEntity();
            employeeRoleRelationEntity.setRoleId(roleId);
            employeeRoleRelationEntity.setEmployeeId(employeeId);
            roleRelationEntities.add(employeeRoleRelationEntity);
        }
        roleEmployeeDao.batchInsert(roleRelationEntities);
        return ResponseDTO.succ();
    }

    /**
     * 通过员工id获取员工角色
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<List<RoleSelectedVO>> getRolesByEmployeeId(Long employeeId) {
        List<Long> roleIds = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        List<RoleVO> roleList = roleDao.selectList(new EntityWrapper());
        List<RoleSelectedVO> result = SmartBeanUtil.copyList(roleList, RoleSelectedVO.class);
        result.stream().forEach(item -> item.setSelected(roleIds.contains(item.getId())));
        return ResponseDTO.succData(result);
    }
}
