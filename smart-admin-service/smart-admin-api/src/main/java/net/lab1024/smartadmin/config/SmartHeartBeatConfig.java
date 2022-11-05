package net.lab1024.smartadmin.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * [  ]
 * 
 * @version 1.0
 * @since JDK1.8
 * @author yandanyang
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 */
@Data
@Configuration
public class SmartHeartBeatConfig {

    /**
     * 延迟执行时间
     */
    @Value("${heart-beat.delayHandlerTime}")
    private Long delayHandlerTime;

    /**
     * 间隔执行时间
     */
    @Value("${heart-beat.intervalTime}")
    private Long intervalTime;
}
