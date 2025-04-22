package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 可用仓喷头
 *
 * @Author 海印: 芦苇
 */
@Data
@TableName("t_usable_sprinkler")
public class UsableSprinklerEntity {
    /**
     * 喷头ID
     */
    @TableId(type = IdType.AUTO)
    private Long sprinklerId;

    /**
     * 喷头序列号
     */
    @DataTracerFieldLabel("喷头序列号")
    private String sprinklerSerial;

    /**
     * 历史
     */
    @DataTracerFieldLabel("历史")
    private String history;

    /**
     * 返仓日期
     */
    @DataTracerFieldLabel("返仓日期")
    private LocalDate retWarehouseDate;




    /**
     * 领用是否有限制
     */
    @DataTracerFieldLabel("领用是否有限制")
    private String allocateLimitation;

    /**
     * 领用时备注1
     */
    @DataTracerFieldLabel("领用时备注1")
    private String allocateNote1;

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
