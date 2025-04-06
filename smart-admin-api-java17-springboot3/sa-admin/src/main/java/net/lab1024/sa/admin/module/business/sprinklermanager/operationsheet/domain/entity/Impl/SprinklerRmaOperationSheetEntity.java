package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.BaseEntity;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_sprinkler_rma_operation_sheet")
@NoArgsConstructor
public class SprinklerRmaOperationSheetEntity extends BaseEntity {

    /**
     * 操作列表ID
     */
    @TableId(type = IdType.AUTO)
    private Long sprinklerStockInOperationSheetId;

    /**
     * 喷头Id
     */
    @DataTracerFieldLabel("喷头Id")
    private Long sprinklerId;

    /**
     * 喷头操作记录id
     */
    @DataTracerFieldLabel("喷头操作记录Id")
    private Long operationSheetId;

    /**
     * 喷头序列号
     */
    @DataTracerFieldLabel("喷头序列号")
    private String sprinklerSerial;

    /**
     * 快递日期
     */
    @DataTracerFieldLabel("快递日期")
    private LocalDate rmaDate;

    /**
     * rma号
     */
    @DataTracerFieldLabel("rma号")
    private String rmaNumber;

    /**
     * 寄件地点
     */
    @DataTracerFieldLabel("寄件地点")
    private String rmaPosition;

    /**
     * rma原因
     */
    @DataTracerFieldLabel("rma原因")
    private String rmaReason;

    /**
     * 快递单号
     */
    @DataTracerFieldLabel("快递单号")
    private String postNumber;

    /**
     * 处理结果
     */
    @DataTracerFieldLabel("处理结果")
    private String processResult;

    /**
     * 是否换新
     */
    @DataTracerFieldLabel("是否换新")
    private String agreeReplacement;

    /**
     * 换回
     */
    @DataTracerFieldLabel("换回")
    private String replacementResult;

    /**
     * 算不算在维修仓
     */
    @DataTracerFieldLabel("算不算在维修仓")
    private String inMaintainWarehouse;

    /**
     * 返修原因
     */
    @DataTracerFieldLabel("返修原因")
    private String maintainReason;

    /**
     * 返修日期
     */
    @DataTracerFieldLabel("返修日期")
    private LocalDate maintainDate;

    /**
     * 返修客户
     */
    @DataTracerFieldLabel("返修客户")
    private String maintainUser;

    /**
     * 历史
     */
    @DataTracerFieldLabel("历史")
    private String history;

    /**
     * 禁用状态
     */
    @DataTracerFieldLabel("禁用状态")
    private Boolean disabledFlag;

    /**
     * 删除状态
     */
    @DataTracerFieldLabel("删除状态")
    private Boolean deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
