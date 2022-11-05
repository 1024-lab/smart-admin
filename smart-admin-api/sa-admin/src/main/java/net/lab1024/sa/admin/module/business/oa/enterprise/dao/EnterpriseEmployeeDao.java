package net.lab1024.sa.admin.module.business.oa.enterprise.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.entity.EnterpriseEmployeeEntity;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.form.EnterpriseEmployeeQueryForm;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseEmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 企业员工
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/7/28 20:37:15
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Mapper
@Component
public interface EnterpriseEmployeeDao extends BaseMapper<EnterpriseEmployeeEntity> {


    /**
     * 根据员工查询
     * @param employeeIdList
     * @return
     */
    List<EnterpriseEmployeeVO> selectByEmployeeIdList(@Param("employeeIdList")Collection<Long> employeeIdList);

    /**
     * 查询员工关联的企业
     * @param employeeId
     * @return
     */
    List<Long> selectEnterpriseIdByEmployeeId(@Param("employeeId")Long employeeId);
    /**
     * 根据企业查询
     * @param enterpriseIdList
     * @return
     */
    List<EnterpriseEmployeeVO> selectByEnterpriseIdList(@Param("enterpriseIdList")Collection<Long> enterpriseIdList);
    /**
     * 根据企业查询
     * @param enterpriseId
     * @return
     */
    List<EnterpriseEmployeeEntity> selectByEnterpriseId(@Param("enterpriseId")Long enterpriseId);

    /**
     * 查询企业下的所有员工id
     * @param enterpriseIdList
     * @return
     */
    List<Long> selectEmployeeIdByEnterpriseIdList(@Param("enterpriseIdList")Collection<Long> enterpriseIdList);
    /**
     * 根据员工删除
     * @param enterpriseId
     * @param employeeIdList
     */
    void deleteByEnterpriseAndEmployeeIdList(@Param("enterpriseId")Long enterpriseId, @Param("employeeIdList")Collection<Long> employeeIdList);

    /**
     * 根据员工查询
     * @param enterpriseId
     * @param employeeIdList
     */
    List<EnterpriseEmployeeEntity> selectByEnterpriseAndEmployeeIdList(@Param("enterpriseId")Long enterpriseId, @Param("employeeIdList")Collection<Long> employeeIdList);

    /**
     * 删除某员工关联的所有企业
     * @param employeeId
     */
    void deleteByEmployeeId(@Param("employeeId")Long employeeId);

    /**
     * 分页查询企业员工
     * @param page
     * @param queryForm
     * @return
     */
    List<EnterpriseEmployeeVO> queryPageEmployeeList(Page<?> page,@Param("queryForm") EnterpriseEmployeeQueryForm queryForm);
}
