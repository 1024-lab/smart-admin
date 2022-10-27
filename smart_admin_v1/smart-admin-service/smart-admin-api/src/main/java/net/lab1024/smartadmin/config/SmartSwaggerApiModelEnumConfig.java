package net.lab1024.smartadmin.config;

import net.lab1024.smartadmin.common.swagger.SmartSwaggerApiModelEnumPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * [ 对于枚举类进行swagger注解，与前端的vue-enum相匹配 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/8/9 0009 上午 9:46
 * @since JDK1.8
 */
@Configuration
@Profile({"dev", "sit", "pre", "prod"})
public class SmartSwaggerApiModelEnumConfig {

    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1)
    public SmartSwaggerApiModelEnumPlugin swaggerEnum(){
        return new SmartSwaggerApiModelEnumPlugin();
    }
}
