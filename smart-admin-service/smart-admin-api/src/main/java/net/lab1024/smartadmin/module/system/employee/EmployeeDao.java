package net.lab1024.smartadmin.module.system.employee;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.module.system.employee.domain.dto.EmployeeDTO;
import net.lab1024.smartadmin.module.system.employee.domain.dto.EmployeeQueryDTO;
import net.lab1024.smartadmin.module.system.employee.domain.dto.EmployeeQueryExportDTO;
import net.lab1024.smartadmin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.smartadmin.module.system.employee.domain.vo.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 员工dao接口
 *
 * @author lidoudou
 * @date 2017年12月19日下午1:36:30
 */
@Mapper
@Component
public interface EmployeeDao extends BaseMapper<EmployeeEntity> {
    /**
     * 查询员工列表
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<EmployeeDTO> selectEmployeeList(Page page, @Param("queryDTO") EmployeeQueryDTO queryDTO);

    /**
     * 不带分页查询员工列表
     * @param queryDTO
     * @return
     */
    List<EmployeeDTO> selectEmployeeList(@Param("queryDTO") EmployeeQueryExportDTO queryDTO);

    /**
     * 批量更新禁用状态
     *
     * @param employeeIds
     * @param isDisabled
     */
    void batchUpdateStatus(@Param("employeeIds") List<Long> employeeIds, @Param("isDisabled") Integer isDisabled);

    /**
     * 登录
     *
     * @param loginName
     * @param loginPwd
     * @return
     */
    EmployeeDTO login(@Param("loginName") String loginName, @Param("loginPwd") String loginPwd);

    /**
     * 通过登录名查询
     *
     * @param loginName
     * @param isDisabled
     * @return
     */
    EmployeeDTO getByLoginName(@Param("loginName") String loginName, @Param("isDisabled") Integer isDisabled);

    /**
     * 通过手机号查询
     *
     * @param phone
     * @param isDisabled
     * @return
     */
    EmployeeDTO getByPhone(@Param("phone") String phone, @Param("isDisabled") Integer isDisabled);

    /**
     * 获取所有员工
     *
     * @return
     */
    List<EmployeeDTO> listAll();

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
    List<EmployeeDTO> getEmployeeByIds(@Param("ids") Collection<Long> employeeIds);


    EmployeeDTO getEmployeeById(@Param("id") Long employeeId);
    /**
     * 获取某个部门的员工
     *
     * @param departmentId
     * @return
     */
    List<EmployeeVO> getEmployeeIdByDeptId(@Param("departmentId") Long departmentId);

    /**
     * 获取某批部门的员工
     *
     * @param departmentIds
     * @return
     */
    List<EmployeeDTO> getEmployeeIdByDeptIds(@Param("departmentIds") List<Long> departmentIds);


    /**
     * 员工重置密码
     *
     * @param employeeId
     * @param password
     * @return
     */
    Integer updatePassword(@Param("employeeId") Integer employeeId, @Param("password") String password);


    /**
     * 查询所有员工
     * @return
     */
    List<EmployeeVO> selectAll();
}