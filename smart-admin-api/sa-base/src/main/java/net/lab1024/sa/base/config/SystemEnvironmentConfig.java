package net.lab1024.sa.base.config;

import net.lab1024.sa.base.common.domain.SystemEnvironment;
import net.lab1024.sa.base.common.enumeration.SystemEnvironmentEnum;
import net.lab1024.sa.base.common.util.SmartEnumUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 系统环境
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021/08/13 18:56
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Configuration
public class SystemEnvironmentConfig implements Condition {

    @Value("${spring.profiles.active}")
    private String systemEnvironment;

    @Value("${project.name}")
    private String projectName;

    /**
     * 判断是否开启swagger
     */
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return isDevOrTest(conditionContext);
    }

    /**
     * 是否为：开发环境和 测试环境
     */
    private boolean isDevOrTest(ConditionContext conditionContext) {
        String property = conditionContext.getEnvironment().getProperty("spring.profiles.active");
        return StringUtils.isNotBlank(property) && (SystemEnvironmentEnum.TEST.equalsValue(property) || SystemEnvironmentEnum.DEV.equalsValue(property));
    }

    @Bean("systemEnvironment")
    public SystemEnvironment initEnvironment() {
        SystemEnvironmentEnum currentEnvironment = SmartEnumUtil.getEnumByValue(systemEnvironment, SystemEnvironmentEnum.class);
        if (currentEnvironment == null) {
            throw new ExceptionInInitializerError("无法获取当前环境！请在 application.yaml 配置参数：spring.profiles.active");
        }
        if (StringUtils.isBlank(projectName)) {
            throw new ExceptionInInitializerError("无法获取当前项目名称！请在 application.yaml 配置参数：project.name");
        }
        return new SystemEnvironment(currentEnvironment == SystemEnvironmentEnum.PROD, projectName, currentEnvironment);
    }
}
