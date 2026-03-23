package net.lab1024.sa.base.module.support.serialnumber.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
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
    @TableId(type= IdType.NONE)
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
