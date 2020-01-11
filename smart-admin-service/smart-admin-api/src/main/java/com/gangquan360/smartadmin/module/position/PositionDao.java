package com.gangquan360.smartadmin.module.position;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.gangquan360.smartadmin.module.position.domain.dto.PositionQueryDTO;
import com.gangquan360.smartadmin.module.position.domain.dto.PositionRelationAddDTO;
import com.gangquan360.smartadmin.module.position.domain.dto.PositionRelationQueryDTO;
import com.gangquan360.smartadmin.module.position.domain.dto.PositionRelationResultDTO;
import com.gangquan360.smartadmin.module.position.domain.entity.PositionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zzr
 */
@Mapper
@Component
public interface PositionDao extends BaseMapper<PositionEntity> {

    /**
     * 查询岗位列表
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<PositionEntity> selectByPage(Pagination page, PositionQueryDTO queryDTO);

    /**
     * 查询岗位与人员关系
     *
     * @param positionRelationQueryDTO
     * @return
     */
    List<PositionRelationResultDTO> selectRelation(PositionRelationQueryDTO positionRelationQueryDTO);

    /**
     * 批量查询员工岗位信息
     * @param employeeIdList
     * @return
     */
    List<PositionRelationResultDTO> selectEmployeesRelation(@Param("employeeIdList") List<Long> employeeIdList);

    /**
     * 批量添加岗位 人员 关联关系
     *
     * @param positionRelationAddDTO
     * @return
     */
    Integer insertBatchRelation(@Param("batchDTO")PositionRelationAddDTO positionRelationAddDTO);

    /**
     * 删除指定人员的 岗位关联关系
     *
     * @param employeeId
     * @return
     */
    Integer deleteRelationByEmployeeId(@Param("employeeId") Long employeeId);

}
