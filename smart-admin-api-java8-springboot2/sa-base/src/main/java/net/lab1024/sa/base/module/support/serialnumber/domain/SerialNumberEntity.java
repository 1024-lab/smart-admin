package net.lab1024.sa.base.module.support.serialnumber.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.sa.base.module.support.serialnumber.constant.SerialNumberRuleTypeEnum;

import java.time.LocalDateTime;

/**
 * 单据序列号 定义表
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-25 21:46:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("t_serial_number")
public class SerialNumberEntity {

    /**
     * 主键id
     *
     * @see SerialNumberIdEnum
     */
    @TableId(type = IdType.INPUT)
    private Integer serialNumberId;

    /**
     * 业务
     */
    private String businessName;

    /**
     * 格式
     */
    private String format;

    /**
     * 生成规则
     *
     * @see SerialNumberRuleTypeEnum
     */
    private String ruleType;


    /**
     * 初始值
     */
    private Long initNumber;

    /**
     * 步长随机数范围
     */
    private Integer stepRandomRange;

    /**
     * 备注
     */
    private String remark;

    /**
     * 上次产生的单号, 默认为空
     */
    private Long lastNumber;

    /**
     * 上次产生的单号时间
     */
    private LocalDateTime lastTime;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
