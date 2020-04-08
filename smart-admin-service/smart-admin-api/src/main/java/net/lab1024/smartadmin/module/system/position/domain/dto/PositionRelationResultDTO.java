package net.lab1024.smartadmin.module.system.position.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * 岗位关联关系
 *
 * @author zzr
 */
@Data
public class PositionRelationResultDTO {

    /**
     * 岗位ID
     */
    private Long positionId;

    /**
     * 员工ID
     */
    private Long employeeId;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

}
