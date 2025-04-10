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
@TableName("t_sprinkler_stock_in_operation_sheet")
@NoArgsConstructor
public class SprinklerStockInOperationSheetEntity implements BaseEntity {

    /**
     * 操作列表ID
     */
    @TableId(type = IdType.AUTO)
    private Long sprinklerStockInOperationSheetId;

    /**
     * 购入日期
     */
    @DataTracerFieldLabel("购入日期")
    private LocalDate purcharseDate;

    /**
     * 合同编号
     */
    @DataTracerFieldLabel("合同编号")
    private String contractNumber;

    /**
     * 喷头Id
     */
    @DataTracerFieldLabel("喷头Id")
    private Long sprinklerId;

    /**
     * 喷头操作列表id
     */
    @DataTracerFieldLabel("喷头操作列表Id")
    private Long operationSheetId;
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

    public Long getOSId(){
        return this.getOperationSheetId();
    }
}
