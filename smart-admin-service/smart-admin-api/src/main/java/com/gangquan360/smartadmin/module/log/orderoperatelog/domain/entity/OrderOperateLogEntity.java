package com.gangquan360.smartadmin.module.log.orderoperatelog.domain.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.gangquan360.smartadmin.common.domain.BaseEntity;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 各种单据操作记录
 *
 * </p>
 *
 * @author anders
 * @since 2018-01-09
 */

@Data
@Builder
@TableName("t_order_operate_log")
public class OrderOperateLogEntity extends BaseEntity{

    /**
     * 各种单据的id
     */
    private Long orderId;
    /**
     * 单据类型
     */
    private Integer orderType;
    /**
     * 操作类型
     */
    private Integer operateType;
    /**
     * 操作类型 对应的中文
     */
    private String operateContent;
    /**
     * 操作备注
     */
    private String operateRemark;
    /**
     * 员工id
     */
    private Long employeeId;
    /**
     * 员工名称
     */
    private String employeeName;
    /**
     * 额外信息
     */
    private String extData;



}
