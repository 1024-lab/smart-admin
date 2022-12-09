package net.lab1024.sa.admin.config;

import net.lab1024.sa.common.common.domain.RequestUser;
import net.lab1024.sa.common.common.util.SmartRequestUtil;
import net.lab1024.sa.common.module.support.jwe.JweAspect;
import net.lab1024.sa.common.module.support.jwe.JweUserKey;
import net.lab1024.sa.common.module.support.operatelog.core.OperateLogAspect;
import net.lab1024.sa.common.module.support.operatelog.core.OperateLogConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 操作日志切面 配置
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-05-30 21:22:12
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Configuration
public class JweAspectConfig {

    /**
     * 配置信息
     */
    @Bean
    public JweAspect jweConfig() {
        return new JweAspect((request -> {
            RequestUser requestUser = SmartRequestUtil.getRequestUser();
            JweUserKey userKey = new JweUserKey();
            userKey.setUserId(requestUser.getUserId());
            userKey.setUserName(requestUser.getUserName());
            userKey.setExtData(requestUser.getUserType().getValue().toString());
            return userKey;
        }));
    }
}