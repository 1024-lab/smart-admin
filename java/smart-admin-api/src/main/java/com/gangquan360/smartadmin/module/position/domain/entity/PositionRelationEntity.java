package com.gangquan360.smartadmin.module.position.domain.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.gangquan360.smartadmin.common.domain.BaseEntity;
import lombok.Data;

/**
 * 岗位关联关系
 *
 * @author zzr
 */
@Data
@TableName("t_position_relation")
public class PositionRelationEntity extends BaseEntity {

    /**
     * 岗位ID
     */
    private Long positionId;

    /**
     * 员工ID
     */
    private Long employeeId;

}
