package net.lab1024.smartadmin.config;
import net.lab1024.smartadmin.constant.SystemEnvironmentEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
/**
 * 是否是正式环境
 *
 * @author listen
 * @date 2019/08/27 08:56
 */
public class SystemEnvironmentCondition implements Condition {

    @Value("${spring.profiles.active}")
    private String systemEnvironment;

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return ! SystemEnvironmentEnum.PROD.equalsValue(systemEnvironment);
    }
}
