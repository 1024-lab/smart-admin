package net.lab1024.sa.admin.config;

import net.lab1024.sa.base.module.support.operatelog.core.OperateLogAspect;
import net.lab1024.sa.base.module.support.operatelog.core.OperateLogConfig;
import org.springframework.context.annotation.Configuration;

/**
 * 操作日志切面 配置
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-05-30 21:22:12
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Configuration
public class OperateLogAspectConfig extends OperateLogAspect{

    /**
     * 配置信息
     */
    @Override
    public OperateLogConfig getOperateLogConfig() {
        return OperateLogConfig.builder().corePoolSize(1).queueCapacity(10000).build();
    }


}