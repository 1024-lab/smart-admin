package net.lab1024.smartadmin.module.business.log.orderoperatelog.domain.dto;


import net.lab1024.smartadmin.module.business.log.orderoperatelog.constant.OrderOperateLogOperateTypeConst;
import net.lab1024.smartadmin.module.business.log.orderoperatelog.constant.OrderOperateLogOrderTypeEnum;
import lombok.Data;

/**
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@Data
public class OrderOperateLogSaveDTO {

    /**
     * 各种单据的id
     */
    private Long orderId;

    /**
     * 单据类型
     */
    private OrderOperateLogOrderTypeEnum orderType;

    /**
     * 操作类型
     */
    private OrderOperateLogOperateTypeConst operateType;

    /**
     *操作类型 对应的中文
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

    public OrderOperateLogSaveDTO() {
    }

    public OrderOperateLogSaveDTO(Long orderId, OrderOperateLogOrderTypeEnum orderType, OrderOperateLogOperateTypeConst operateType, String
        operateRemark, Long employeeId, String employeeName, String extData) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.operateType = operateType;
        this.operateRemark = operateRemark;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.extData = extData;
    }

    @Override
    public String toString() {
        return "OrderOperateLogSaveDTO{" + "orderId=" + orderId + ", orderType=" + orderType + ", operateType=" + operateType + ", operateRemark='"
            + operateRemark + '\'' + ", employeeId=" + employeeId + ", employeeName='" + employeeName + '\'' + ", extData='" + extData + '\'' + '}';
    }

}
