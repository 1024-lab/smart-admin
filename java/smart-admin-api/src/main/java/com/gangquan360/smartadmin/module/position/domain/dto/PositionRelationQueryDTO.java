package com.gangquan360.smartadmin.module.position.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 岗位关系
 *
 * @author zzr
 */
@Data
public class PositionRelationQueryDTO {

    @ApiModelProperty("岗位ID")
    private Long positionId;

    @ApiModelProperty("员工ID")
    private Long employeeId;

}
