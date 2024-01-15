package net.lab1024.sa.base.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.constant.RequestHeaderConst;
import net.lab1024.sa.base.common.swagger.SmartOperationCustomizer;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * springdoc-openapi 配置
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2020-03-25 22:54:46
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Configuration
@Conditional(SystemEnvironmentConfig.class)
public class SwaggerConfig {

    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/swagger-ui.html",
            "/swagger-ui.html/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/doc.html",
    };

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(components())
                .info(new Info()
                        .title("SmartAdmin 3.X 接口文档")
                        .contact(new Contact().name("1024创新实验室").email("lab1024@163.com").url("https://1024lab.net"))
                        .version("v3.X")
                        .description("<font color=\"#DC143C\">**以「高质量代码」为核心，「简洁、高效、安全」**</font>基于 SpringBoot + Sa-Token + Mybatis-Plus 和 Vue3 + Vite5 + Ant Design (同时支持JavaScript和TypeScript双版本) 的快速开发平台。" +
                                "<br/><font color=\"#DC143C\">**国内首个满足《网络安全》、《数据安全》、三级等保**</font>， 支持登录限制、支持国产接口加解密等安全、支持数据加解密等一系列安全体系的开源项目。" +
                                "<br/><font color=\"#DC143C\">**我们开源一套漂亮的代码和一套整洁的代码规范**</font>，让大家在这浮躁的代码世界里感受到一股把代码写好的清流！同时又让开发者节省大量的时间，减少加班，快乐工作，保持谦逊，保持学习，热爱代码，更热爱生活！")
                )
                .addSecurityItem(new SecurityRequirement().addList(RequestHeaderConst.TOKEN));
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes(RequestHeaderConst.TOKEN, new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER).name(RequestHeaderConst.TOKEN));
    }

    @Bean
    public GroupedOpenApi businessApi() {
        return GroupedOpenApi.builder()
                .group("业务接口")
                .pathsToMatch("/**")
                .pathsToExclude(SwaggerTagConst.Support.URL_PREFIX + "/**")
                .addOperationCustomizer(new SmartOperationCustomizer())
                .build();

    }

    @Bean
    public GroupedOpenApi supportApi() {
        return GroupedOpenApi.builder()
                .group("支撑接口(Support)")
                .pathsToMatch(SwaggerTagConst.Support.URL_PREFIX + "/**")
                .addOperationCustomizer(new SmartOperationCustomizer())
                .build();
    }
}
