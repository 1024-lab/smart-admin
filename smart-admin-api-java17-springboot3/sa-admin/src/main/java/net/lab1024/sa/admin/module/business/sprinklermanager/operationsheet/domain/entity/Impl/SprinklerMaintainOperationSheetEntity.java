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
@TableName("t_sprinkler_maintain_operation_sheet")
@NoArgsConstructor
public class SprinklerMaintainOperationSheetEntity extends BaseEntity {

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
     * 返修日期
     */
    @DataTracerFieldLabel("返修日期")
    private LocalDate maintainDate;

    /**
     * 返修原因
     */
    @DataTracerFieldLabel("返修原因")
    private String maintainReason;

    /**
     * 具体原因
     */
    @DataTracerFieldLabel("具体原因")
    private String realReason;

    /**
     * 返修机台
     */
    @DataTracerFieldLabel("返修机台")
    private String maintainMachine;

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
