package net.lab1024.sa.admin.module.business.oa.bank.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;

/**
 * OA办公-OA银行信息
 *
 * @Author 1024创新实验室:善逸
 * @Date 2022/6/23 21:59:22
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("t_oa_bank")
public class BankEntity {

    /**
     * 银行信息ID
     */
    @TableId(type = IdType.AUTO)
    @DataTracerFieldLabel("银行信息ID")
    private Long bankId;

    /**
     * 开户银行
     */
    @DataTracerFieldLabel("开户银行")
    private String bankName;

    /**
     * 账户名称
     */
    @DataTracerFieldLabel("账户名称")
    private String accountName;

    /**
     * 账号
     */
    @DataTracerFieldLabel("账号")
    private String accountNumber;

    /**
     * 备注
     */
    @DataTracerFieldLabel("备注")
    private String remark;

    /**
     * 是否对公
     */
    @DataTracerFieldLabel("是否对公")
    private Boolean businessFlag;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 禁用状态
     */
    @DataTracerFieldLabel("禁用状态")
    private Boolean disabledFlag;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人ID
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
