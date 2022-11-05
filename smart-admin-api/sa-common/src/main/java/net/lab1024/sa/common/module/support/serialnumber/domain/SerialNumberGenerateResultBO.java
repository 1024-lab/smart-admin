package net.lab1024.sa.common.module.support.serialnumber.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 单据序列号 生成结果
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-25 21:46:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SerialNumberGenerateResultBO {

    /**
     * 序号id
     */
    private Integer serialNumberId;

    /**
     *  是否重置的初始值
     */
    private Boolean isReset;

    /**
     * 上次生成的数字
     */
    private Long lastNumber;

    /**
     * 上次生成的时间
     */
    private LocalDateTime lastTime;

    /**
     * 生成的 number  集合
     */
    private List<Long> numberList;


}
