package net.lab1024.sa.admin.config;

import net.lab1024.sa.admin.module.system.login.domain.LoginEmployeeDetail;
import net.lab1024.sa.common.common.annoation.SaAuth;
import net.lab1024.sa.common.common.security.SecurityMethodSource;
import net.lab1024.sa.common.common.security.SecurityPermissionCheckService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.expression.method.ExpressionBasedAnnotationAttributeFactory;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;

/**
 * 1、以类名加方法名为权限字符串的校验模式 <br>
 * 2、重写MethodSecurityMetadataSource将优化security配置，只需在方法上加上@saAuth注解，方法上就会存在权限（权限字符串为类名加方法名），而无需另外手动设置，减轻后端开发成本<br>
 * 3、security将不再依据权限字符串进行权限控制，<br>
 * 4、security将依据对应权限字符串下的接口权限进行控制 <br>
 * 5、采用此配置原@PreAuthorize依然有效 <br>
 * 6、如若无需此配置，需将@EnableGlobalMethodSecurity注解添加至SecurityConfig类上
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021-08-31 0:01
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityMethodConfig extends GlobalMethodSecurityConfiguration {

    @Bean(SaAuth.saAuth)
    public SecurityPermissionCheckService securityPermissionCheckService() {
        return new SecurityPermissionCheckService() {
            @Override
            public boolean checkPermission(Authentication authentication, String permission) {
                LoginEmployeeDetail loginEmployeeDetail = (LoginEmployeeDetail) authentication.getPrincipal();
                if (loginEmployeeDetail.getAdministratorFlag()) {
                    return true;
                }
                return super.permissionJudge(loginEmployeeDetail, permission);
            }
        };
    }

    @Override
    public MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        ExpressionBasedAnnotationAttributeFactory attributeFactory = new ExpressionBasedAnnotationAttributeFactory(this.getExpressionHandler());
        return new SecurityMethodSource(attributeFactory, SaAuth.saAuth);
    }
}
