package net.lab1024.sa.admin.module.system.department.service;

import net.lab1024.sa.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.sa.admin.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.sa.admin.module.system.department.domain.form.DepartmentAddForm;
import net.lab1024.sa.admin.module.system.department.domain.form.DepartmentUpdateForm;
import net.lab1024.sa.admin.module.system.department.domain.vo.DepartmentTreeVO;
import net.lab1024.sa.admin.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.sa.admin.module.system.department.manager.DepartmentCacheManager;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 部门 service
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-12 20:37:48
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class DepartmentService {

    @Resource
    private DepartmentDao departmentDao;

    @Resource
    private EmployeeDao employeeDao;

    @Resource
    private DepartmentCacheManager departmentCacheManager;

    // ---------------------------- 增加、修改、删除 ----------------------------

    /**
     * 新增添加部门
     *
     */

    public ResponseDTO<String> addDepartment(DepartmentAddForm departmentAddForm) {
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(departmentAddForm, DepartmentEntity.class);
        departmentDao.insert(departmentEntity);
        this.clearCache();
        return ResponseDTO.ok();
    }


    /**
     * 更新部门信息
     *
     */
    public ResponseDTO<String> updateDepartment(DepartmentUpdateForm updateDTO) {
        if (updateDTO.getParentId() == null) {
            return ResponseDTO.userErrorParam("父级部门id不能为空");
        }
        DepartmentEntity entity = departmentDao.selectById(updateDTO.getDepartmentId());
        if (entity == null) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(updateDTO, DepartmentEntity.class);
        departmentEntity.setSort(updateDTO.getSort());
        departmentDao.updateById(departmentEntity);
        this.clearCache();
        return ResponseDTO.ok();
    }

    /**
     * 根据id删除部门
     * 1、需要判断当前部门是否有子部门,有子部门则不允许删除
     * 2、需要判断当前部门是否有员工，有员工则不能删除
     *
     */
    public ResponseDTO<String> deleteDepartment(Long departmentId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (null == departmentEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        // 是否有子级部门
        int subDepartmentNum = departmentDao.countSubDepartment(departmentId);
        if (subDepartmentNum > 0) {
            return ResponseDTO.userErrorParam("请先删除子级部门");
        }

        // 是否有未删除员工
        int employeeNum = employeeDao.countByDepartmentId(departmentId, Boolean.FALSE);
        if (employeeNum > 0) {
            return ResponseDTO.userErrorParam("请先删除部门员工");
        }
        departmentDao.deleteById(departmentId);
        // 清除缓存
        this.clearCache();
        return ResponseDTO.ok();
    }

    /**
     * 清除自身以及下级的id列表缓存
     */
    private void clearCache() {
        departmentCacheManager.clearCache();
    }

    // ---------------------------- 查询 ----------------------------

    /**
     * 获取部门树形结构
     */
    public ResponseDTO<List<DepartmentTreeVO>> departmentTree() {
        List<DepartmentTreeVO> treeVOList = departmentCacheManager.getDepartmentTree();
        return ResponseDTO.ok(treeVOList);
    }


    /**
     * 自身以及所有下级的部门id列表
     *
     */
    public List<Long> selfAndChildrenIdList(Long departmentId) {
        return departmentCacheManager.getDepartmentSelfAndChildren(departmentId);
    }


    /**
     * 获取所有部门
     *
     */
    public List<DepartmentVO> listAll() {
        return departmentCacheManager.getDepartmentList();
    }


    /**
     * 获取部门
     *
     */
    public DepartmentVO getDepartmentById(Long departmentId) {
        return departmentCacheManager.getDepartmentMap().get(departmentId);
    }

    /**
     * 获取部门路径：/公司/研发部/产品组
     */
    public String getDepartmentPath(Long departmentId) {
        return departmentCacheManager.getDepartmentPathMap().get(departmentId);
    }

    /**
     * 查询全部父级部门（不包含自己）
     *
     */
    public List<DepartmentVO> queryAllParentDepartment(Long departmentId) {
        List<DepartmentVO> list = new ArrayList<>();

        Map<Long, DepartmentVO> departmentMap = departmentCacheManager.getDepartmentMap();
        DepartmentVO departmentVO = departmentMap.get(departmentId);
        while (departmentVO != null) {
            list.add(departmentVO);
            departmentVO = departmentMap.get(departmentVO.getParentId());
        }
        Collections.reverse(list);
        return list;
    }

    /**
     * 查询全部父级部门（不包含自己）
     *
     */
    public List<Long> queryAllParentDepartmentIdList(Long departmentId) {
        List<Long> list = new ArrayList<>();

        Map<Long, DepartmentVO> departmentMap = departmentCacheManager.getDepartmentMap();
        DepartmentVO departmentVO = departmentMap.get(departmentId);
        while (departmentVO != null) {
            list.add(departmentVO.getDepartmentId());
            departmentVO = departmentMap.get(departmentVO.getParentId());
        }
        Collections.reverse(list);
        return list;
    }

}
