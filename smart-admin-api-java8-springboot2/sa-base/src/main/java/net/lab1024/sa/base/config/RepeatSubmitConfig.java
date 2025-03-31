package net.lab1024.sa.base.config;

import net.lab1024.sa.base.common.constant.StringConst;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.repeatsubmit.RepeatSubmitAspect;
import net.lab1024.sa.base.module.support.repeatsubmit.ticket.RepeatSubmitCaffeineTicket;
import net.lab1024.sa.base.module.support.repeatsubmit.ticket.RepeatSubmitRedisTicket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * 重复提交配置
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021/10/9 18:47
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Configuration
public class RepeatSubmitConfig {

    @Resource
    private ValueOperations<String, String> valueOperations;

    @Bean
    public RepeatSubmitAspect repeatSubmitAspect() {
        RepeatSubmitRedisTicket caffeineTicket = new RepeatSubmitRedisTicket(valueOperations, this::ticket);
        return new RepeatSubmitAspect(caffeineTicket);
    }

    /**
     * 获取指明某个用户的凭证
     */
    private String ticket(String servletPath) {
        Long userId = SmartRequestUtil.getRequestUserId();
        if (null == userId) {
            return StringConst.EMPTY;
        }
        return servletPath + "_" + userId;
    }
}
