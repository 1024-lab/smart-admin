package net.lab1024.sa.admin.module.system.employee.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.sa.admin.module.system.employee.domain.form.EmployeeQueryForm;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 员工 dao
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021-12-09 22:57:49
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Mapper
@Component
public interface EmployeeDao extends BaseMapper<EmployeeEntity> {
    /**
     * 查询员工列表
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<EmployeeVO> queryEmployee(Page page, @Param("queryForm") EmployeeQueryForm queryForm, @Param("departmentIdList") List<Long> departmentIdList);

    /**
     * 查询员工
     */
    List<EmployeeVO> selectEmployeeByDisabledAndDeleted(@Param("disabledFlag") Boolean disabledFlag, @Param("deletedFlag") Boolean deletedFlag);


    /**
     * 更新单个
     *
     * @param employeeId
     * @param disabledFlag
     */
    void updateDisableFlag(@Param("employeeId") Long employeeId, @Param("disabledFlag") Boolean disabledFlag);


    /**
     * 通过登录名查询
     *
     * @param loginName
     * @param disabledFlag
     * @return
     */
    EmployeeEntity getByLoginName(@Param("loginName") String loginName,
                                  @Param("disabledFlag") Boolean disabledFlag);


    /**
     * 通过姓名查询
     *
     * @param actualName
     * @param disabledFlag
     * @return
     */
    EmployeeEntity getByActualName(@Param("actualName") String actualName,
                                   @Param("disabledFlag") Boolean disabledFlag
    );

    /**
     * 通过手机号查询
     *
     * @param phone
     * @param disabledFlag
     * @return
     */
    EmployeeEntity getByPhone(@Param("phone") String phone, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 获取所有员工
     *
     * @return
     */
    List<EmployeeVO> listAll();

    /**
     * 获取某个部门员工数
     *
     * @param departmentId
     * @return
     */
    Integer countByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 获取一批员工
     *
     * @param employeeIds
     * @return
     */
    List<EmployeeVO> getEmployeeByIds(@Param("employeeIds") Collection<Long> employeeIds);


    /**
     * 查询单个员工信息
     *
     * @param employeeId
     * @return
     */
    EmployeeVO getEmployeeById(@Param("employeeId") Long employeeId);


    /**
     * 获取某个部门的员工
     *
     * @param departmentId
     * @param disabledFlag
     * @return
     */
    List<EmployeeEntity> selectByDepartmentId(@Param("departmentId") Long departmentId, @Param("disabledFlag") Boolean disabledFlag);


    /**
     * 查询某些部门下用户名是xxx的员工
     *
     * @param departmentIdList
     * @param actualName
     * @param disabledFlag
     * @return
     */
    List<EmployeeEntity> selectByActualName(@Param("departmentIdList") List<Long> departmentIdList, @Param("actualName") String actualName, @Param("disabledFlag") Boolean disabledFlag);


    /**
     * 获取某批部门的员工Id
     *
     * @param departmentIds
     * @return
     */
    List<Long> getEmployeeIdByDepartmentIdList(@Param("departmentIds") List<Long> departmentIds, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 获取所有
     *
     * @param leaveFlag
     * @param disabledFlag
     * @return
     */
    List<Long> getEmployeeId(@Param("leaveFlag") Boolean leaveFlag, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 获取某个部门的员工Id
     *
     * @param departmentId
     * @param disabledFlag
     * @return
     */
    List<Long> getEmployeeIdByDepartmentId(@Param("departmentId") Long departmentId, @Param("disabledFlag") Boolean disabledFlag);

    /**
     * 员工重置密码
     *
     * @param employeeId
     * @param password
     * @return
     */
    Integer updatePassword(@Param("employeeId") Integer employeeId, @Param("password") String password);

}