package net.lab1024.sa.admin.module.system.role.service;

import net.lab1024.sa.admin.module.system.role.dao.RoleDao;
import net.lab1024.sa.admin.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.sa.admin.module.system.role.dao.RoleMenuDao;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleEntity;
import net.lab1024.sa.admin.module.system.role.domain.form.RoleAddForm;
import net.lab1024.sa.admin.module.system.role.domain.form.RoleUpdateForm;
import net.lab1024.sa.admin.module.system.role.domain.vo.RoleVO;
import net.lab1024.sa.common.common.code.UserErrorCode;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.common.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021-08-16 20:19:22
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleMenuDao roleMenuDao;

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    /**
     * 新增添加角色
     *
     * @param roleAddForm
     * @return ResponseDTO
     */
    public ResponseDTO addRole(RoleAddForm roleAddForm) {
        RoleEntity existRoleEntity = roleDao.getByRoleName(roleAddForm.getRoleName());
        if (null != existRoleEntity) {
            return ResponseDTO.userErrorParam("角色名称重复");
        }
        RoleEntity roleEntity = SmartBeanUtil.copy(roleAddForm, RoleEntity.class);
        roleDao.insert(roleEntity);
        return ResponseDTO.ok();
    }

    /**
     * 根据角色id 删除
     *
     * @param roleId
     * @return ResponseDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO deleteRole(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        roleDao.deleteById(roleId);
        roleMenuDao.deleteByRoleId(roleId);
        roleEmployeeDao.deleteByRoleId(roleId);
        return ResponseDTO.ok();
    }

    /**
     * 更新角色
     *
     * @param roleUpdateForm
     * @return ResponseDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRole(RoleUpdateForm roleUpdateForm) {
        if (null == roleDao.selectById(roleUpdateForm.getRoleId())) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        RoleEntity existRoleEntity = roleDao.getByRoleName(roleUpdateForm.getRoleName());
        if (null != existRoleEntity && !existRoleEntity.getRoleId().equals(roleUpdateForm.getRoleId())) {
            return ResponseDTO.userErrorParam("角色名称重复");
        }
        RoleEntity roleEntity = SmartBeanUtil.copy(roleUpdateForm, RoleEntity.class);
        roleDao.updateById(roleEntity);
        return ResponseDTO.ok();
    }

    /**
     * 根据id获取角色数据
     *
     * @param roleId
     * @return ResponseDTO
     */
    public ResponseDTO<RoleVO> getRoleById(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        RoleVO role = SmartBeanUtil.copy(roleEntity, RoleVO.class);
        return ResponseDTO.ok(role);
    }

    /**
     * 获取所有角色列表
     *
     * @return ResponseDTO
     */
    public ResponseDTO<List<RoleVO>> getAllRole() {
        List<RoleEntity> roleEntityList = roleDao.selectList(null);
        List<RoleVO> roleList = SmartBeanUtil.copyList(roleEntityList, RoleVO.class);
        return ResponseDTO.ok(roleList);
    }
}
