package net.lab1024.sa.admin.module.system.employee.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.sa.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.sa.admin.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.sa.admin.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.sa.admin.module.system.department.service.DepartmentService;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.sa.admin.module.system.employee.domain.form.*;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.sa.admin.module.system.employee.manager.EmployeeManager;
import net.lab1024.sa.admin.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.sa.admin.module.system.role.domain.vo.RoleEmployeeVO;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.constant.StringConst;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.enumeration.UserTypeEnum;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.securityprotect.service.ProtectPasswordService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工 service
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021-12-29 21:52:46
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class EmployeeService {

    private static final String PASSWORD_SALT_FORMAT = "smart_%s_admin_$^&*";

    @Resource
    private EmployeeDao employeeDao;

    @Resource
    private DepartmentDao departmentDao;

    @Resource
    private EmployeeManager employeeManager;

    @Resource
    private RoleEmployeeDao roleEmployeeDao;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private ProtectPasswordService protectPasswordService;

    public EmployeeEntity getById(Long employeeId) {
        return employeeDao.selectById(employeeId);
    }


    /**
     * 查询员工列表
     *
     */
    public ResponseDTO<PageResult<EmployeeVO>> queryEmployee(EmployeeQueryForm employeeQueryForm) {
        employeeQueryForm.setDeletedFlag(false);
        Page pageParam = SmartPageUtil.convert2PageQuery(employeeQueryForm);

        List<Long> departmentIdList = new ArrayList<>();
        if (employeeQueryForm.getDepartmentId() != null) {
            departmentIdList.addAll(departmentService.selfAndChildrenIdList(employeeQueryForm.getDepartmentId()));
        }

        List<EmployeeVO> employeeList = employeeDao.queryEmployee(pageParam, employeeQueryForm, departmentIdList);
        if (CollectionUtils.isEmpty(employeeList)) {
            PageResult<EmployeeVO> pageResult = SmartPageUtil.convert2PageResult(pageParam, employeeList);
            return ResponseDTO.ok(pageResult);
        }

        List<Long> employeeIdList = employeeList.stream().map(EmployeeVO::getEmployeeId).collect(Collectors.toList());
        // 查询员工角色
        List<RoleEmployeeVO> roleEmployeeEntityList = roleEmployeeDao.selectRoleByEmployeeIdList(employeeIdList);
        Map<Long, List<Long>> employeeRoleIdListMap = roleEmployeeEntityList.stream().collect(Collectors.groupingBy(RoleEmployeeVO::getEmployeeId, Collectors.mapping(RoleEmployeeVO::getRoleId, Collectors.toList())));
        Map<Long, List<String>> employeeRoleNameListMap = roleEmployeeEntityList.stream().collect(Collectors.groupingBy(RoleEmployeeVO::getEmployeeId, Collectors.mapping(RoleEmployeeVO::getRoleName, Collectors.toList())));

        employeeList.forEach(e -> {
            e.setRoleIdList(employeeRoleIdListMap.getOrDefault(e.getEmployeeId(), Lists.newArrayList()));
            e.setRoleNameList(employeeRoleNameListMap.getOrDefault(e.getEmployeeId(), Lists.newArrayList()));
            e.setDepartmentName(departmentService.getDepartmentPath(e.getDepartmentId()));
        });
        PageResult<EmployeeVO> pageResult = SmartPageUtil.convert2PageResult(pageParam, employeeList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 新增员工
     *
     */
    public synchronized ResponseDTO<String> addEmployee(EmployeeAddForm employeeAddForm) {
        // 校验名称是否重复
        EmployeeEntity employeeEntity = employeeDao.getByLoginName(employeeAddForm.getLoginName(), null);
        if (null != employeeEntity) {
            return ResponseDTO.userErrorParam("登录名重复");
        }
        // 校验姓名是否重复
        employeeEntity = employeeDao.getByActualName(employeeAddForm.getActualName(), null);
        if (null != employeeEntity) {
            return ResponseDTO.userErrorParam("姓名重复");
        }
        // 校验电话是否存在
        employeeEntity = employeeDao.getByPhone(employeeAddForm.getPhone(), null);
        if (null != employeeEntity) {
            return ResponseDTO.userErrorParam("手机号已存在");
        }
        // 部门是否存在
        Long departmentId = employeeAddForm.getDepartmentId();
        DepartmentEntity department = departmentDao.selectById(departmentId);
        if (department == null) {
            return ResponseDTO.userErrorParam("部门不存在");
        }

        EmployeeEntity entity = SmartBeanUtil.copy(employeeAddForm, EmployeeEntity.class);

        // 设置密码 默认密码
        String password = protectPasswordService.randomPassword();
        entity.setLoginPwd(getEncryptPwd(password));

        // 保存数据
        entity.setDeletedFlag(Boolean.FALSE);
        employeeManager.saveEmployee(entity, employeeAddForm.getRoleIdList());

        return ResponseDTO.ok(password);
    }

    /**
     * 更新员工
     *
     */
    public synchronized ResponseDTO<String> updateEmployee(EmployeeUpdateForm employeeUpdateForm) {

        Long employeeId = employeeUpdateForm.getEmployeeId();
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 部门是否存在
        Long departmentId = employeeUpdateForm.getDepartmentId();
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.userErrorParam("部门不存在");
        }


        EmployeeEntity existEntity = employeeDao.getByLoginName(employeeUpdateForm.getLoginName(), null);
        if (null != existEntity && !Objects.equals(existEntity.getEmployeeId(), employeeId)) {
            return ResponseDTO.userErrorParam("登录名重复");
        }

        existEntity = employeeDao.getByPhone(employeeUpdateForm.getPhone(), null);
        if (null != existEntity && !Objects.equals(existEntity.getEmployeeId(), employeeId)) {
            return ResponseDTO.userErrorParam("手机号已存在");
        }

        existEntity = employeeDao.getByActualName(employeeUpdateForm.getActualName(), null);
        if (null != existEntity && !Objects.equals(existEntity.getEmployeeId(), employeeId)) {
            return ResponseDTO.userErrorParam("姓名重复");
        }

        // 不更新密码
        EmployeeEntity entity = SmartBeanUtil.copy(employeeUpdateForm, EmployeeEntity.class);
        entity.setLoginPwd(null);

        // 更新数据
        employeeManager.updateEmployee(entity, employeeUpdateForm.getRoleIdList());

        return ResponseDTO.ok();
    }

    /**
     * 更新禁用/启用状态
     *
     */
    public ResponseDTO<String> updateDisableFlag(Long employeeId) {
        if (null == employeeId) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        employeeDao.updateDisableFlag(employeeId, !employeeEntity.getDisabledFlag());

        if (employeeEntity.getDisabledFlag()) {
            // 强制退出登录
            StpUtil.logout(UserTypeEnum.ADMIN_EMPLOYEE.getValue() + StringConst.COLON + employeeId);
        }

        return ResponseDTO.ok();
    }

    /**
     * 批量删除员工
     *
     */
    public ResponseDTO<String> batchUpdateDeleteFlag(List<Long> employeeIdList) {
        if (CollectionUtils.isEmpty(employeeIdList)) {
            return ResponseDTO.ok();
        }
        List<EmployeeEntity> employeeEntityList = employeeManager.listByIds(employeeIdList);
        if (CollectionUtils.isEmpty(employeeEntityList)) {
            return ResponseDTO.ok();
        }
        // 更新删除
        List<EmployeeEntity> deleteList = employeeIdList.stream().map(e -> {
            EmployeeEntity updateEmployee = new EmployeeEntity();
            updateEmployee.setEmployeeId(e);
            updateEmployee.setDeletedFlag(true);
            return updateEmployee;
        }).collect(Collectors.toList());
        employeeManager.updateBatchById(deleteList);

        for (Long employeeId : employeeIdList) {
            // 强制退出登录
            StpUtil.logout(UserTypeEnum.ADMIN_EMPLOYEE.getValue() + StringConst.COLON + employeeId);
        }
        return ResponseDTO.ok();
    }


    /**
     * 批量更新部门
     *
     */
    public ResponseDTO<String> batchUpdateDepartment(EmployeeBatchUpdateDepartmentForm batchUpdateDepartmentForm) {
        List<Long> employeeIdList = batchUpdateDepartmentForm.getEmployeeIdList();
        List<EmployeeEntity> employeeEntityList = employeeDao.selectBatchIds(employeeIdList);
        if (employeeIdList.size() != employeeEntityList.size()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 更新
        List<EmployeeEntity> updateList = employeeIdList.stream().map(e -> {
            EmployeeEntity updateEmployee = new EmployeeEntity();
            updateEmployee.setEmployeeId(e);
            updateEmployee.setDepartmentId(batchUpdateDepartmentForm.getDepartmentId());
            return updateEmployee;
        }).collect(Collectors.toList());
        employeeManager.updateBatchById(updateList);

        return ResponseDTO.ok();
    }


    /**
     * 更新密码
     *
     */
    public ResponseDTO<String> updatePassword(EmployeeUpdatePasswordForm updatePasswordForm) {
        Long employeeId = updatePasswordForm.getEmployeeId();
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (employeeEntity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 校验原始密码
        String encryptPwd = getEncryptPwd(updatePasswordForm.getOldPassword());
        if (!Objects.equals(encryptPwd, employeeEntity.getLoginPwd())) {
            return ResponseDTO.userErrorParam("原密码有误，请重新输入");
        }

        // 新旧密码相同
        String newPassword = updatePasswordForm.getNewPassword();
        if (Objects.equals(updatePasswordForm.getOldPassword(), newPassword)) {
            return ResponseDTO.userErrorParam("新密码与原始密码相同，请重新输入");
        }

        // 校验密码复杂度
        ResponseDTO<String> validatePassComplexity = protectPasswordService.validatePassComplexity(newPassword);
        if (!validatePassComplexity.getOk()) {
            return validatePassComplexity;
        }

        // 更新密码
        EmployeeEntity updateEntity = new EmployeeEntity();
        updateEntity.setEmployeeId(employeeId);
        updateEntity.setLoginPwd(getEncryptPwd(newPassword));
        employeeDao.updateById(updateEntity);

        return ResponseDTO.ok();
    }

    /**
     * 获取某个部门的员工信息
     *
     */
    public ResponseDTO<List<EmployeeVO>> getAllEmployeeByDepartmentId(Long departmentId, Boolean disabledFlag) {
        List<EmployeeEntity> employeeEntityList = employeeDao.selectByDepartmentId(departmentId, disabledFlag);
        if (disabledFlag != null) {
            employeeEntityList = employeeEntityList.stream().filter(e -> e.getDisabledFlag().equals(disabledFlag)).collect(Collectors.toList());
        }

        if (CollectionUtils.isEmpty(employeeEntityList)) {
            return ResponseDTO.ok(Collections.emptyList());
        }

        DepartmentVO department = departmentService.getDepartmentById(departmentId);

        List<EmployeeVO> voList = employeeEntityList.stream().map(e -> {
            EmployeeVO employeeVO = SmartBeanUtil.copy(e, EmployeeVO.class);
            if (department != null) {
                employeeVO.setDepartmentName(department.getName());
            }
            return employeeVO;
        }).collect(Collectors.toList());
        return ResponseDTO.ok(voList);
    }


    /**
     * 重置密码
     *
     */
    public ResponseDTO<String> resetPassword(Integer employeeId) {
        String password = protectPasswordService.randomPassword();
        employeeDao.updatePassword(employeeId, getEncryptPwd(password));
        return ResponseDTO.ok(password);
    }

    /**
     * 获取 加密后 的密码
     *
     */
    public static String getEncryptPwd(String password) {
        return DigestUtils.md5Hex(String.format(PASSWORD_SALT_FORMAT, password));
    }


    /**
     * 查询全部员工
     *
     */
    public ResponseDTO<List<EmployeeVO>> queryAllEmployee(Boolean disabledFlag) {
        List<EmployeeVO> employeeList = employeeDao.selectEmployeeByDisabledAndDeleted(disabledFlag, Boolean.FALSE);
        return ResponseDTO.ok(employeeList);
    }

    /**
     * 根据登录名获取员工
     *
     */
    public EmployeeEntity getByLoginName(String loginName) {
        return employeeDao.getByLoginName(loginName, null);
    }

}
