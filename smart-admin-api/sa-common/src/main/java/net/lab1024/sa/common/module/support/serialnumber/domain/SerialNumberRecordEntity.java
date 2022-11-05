package net.lab1024.sa.common.module.support.serialnumber.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 单据序列号 表结构
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-25 21:46:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_serial_number_record")
public class SerialNumberRecordEntity {

    /**
     * 单号id
     */
    private Integer serialNumberId;

    /**
     * 记录日期
     */
    private LocalDate recordDate;

    /**
     * 最后更新值
     */
    private Long lastNumber;

    /**
     * 上次生成时间
     */
    private LocalDateTime lastTime;

    /**
     * 数量
     */
    private Long count;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

}
