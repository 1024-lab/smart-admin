package net.lab1024.smartadmin.module.business.log.orderoperatelog.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预存款申请/提取单流水临时文件
 * @author zzr
 */
@Data
public class SupplierOrderOperateVO {

    /**
     * 流水类型
     */
    private Integer tradingType;

    /**
     * 总重
     */
    private BigDecimal totalWeight;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 操作人名称
     */
    private String buyerName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;
}
