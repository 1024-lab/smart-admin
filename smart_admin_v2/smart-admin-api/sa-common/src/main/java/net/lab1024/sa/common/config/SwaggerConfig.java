package net.lab1024.sa.common.config;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.constant.RequestHeaderConst;
import net.lab1024.sa.common.common.swagger.SwaggerApiModelPropertyEnumPlugin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据SwaggerTagConst内部类动态生成Swagger group
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2020-03-25 22:54:46
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
@EnableSwagger2
@Configuration
@Conditional(SystemEnvironmentConfig.class)
public class SwaggerConfig implements EnvironmentAware, BeanDefinitionRegistryPostProcessor {

    /**
     * 文档标题
     */
    private String title;

    /**
     * 文档描述
     */
    private String description;

    /**
     * api版本
     */
    private String version;

    /**
     * service url
     */
    private String teamUrl;

    /**
     * host
     */
    private String host;

    private String tagClass;

    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1)
    public SwaggerApiModelPropertyEnumPlugin swaggerEnum() {
        return new SwaggerApiModelPropertyEnumPlugin();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.title = environment.getProperty("swagger.title");
        this.description = environment.getProperty("swagger.description");
        this.version = environment.getProperty("swagger.version");
        this.host = environment.getProperty("swagger.host");
        this.tagClass = environment.getProperty("swagger.tag-class");
        this.teamUrl = environment.getProperty("swagger.team-url");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        Map<String, List<String>> groupMap = this.buildGroup();
        for (Map.Entry<String, List<String>> entry : groupMap.entrySet()) {
            String group = entry.getKey();
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Docket.class, () -> this.baseDocket(group, entry.getValue()));
            BeanDefinition beanDefinition = builder.getRawBeanDefinition();
            registry.registerBeanDefinition(group + "Api", beanDefinition);
        }
    }

    @SneakyThrows
    private Map<String, List<String>> buildGroup() {
        Class<?> clazz = Class.forName(tagClass);
        Class<?>[] innerClazz = clazz.getClasses();
        Map<String, List<String>> groupMap = new HashMap<>(16);
        for (Class<?> cls : innerClazz) {
            String group = cls.getSimpleName();
            List<String> apiTags = Lists.newArrayList();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                boolean isFinal = Modifier.isFinal(field.getModifiers());
                if (isFinal) {
                    apiTags.add(field.get(null).toString());
                }
            }
            groupMap.put(group, apiTags);
        }
        return groupMap;
    }

    private Docket baseDocket(String groupName, List<String> apiTagList) {
        // 配置全局参数
        List<Parameter> parameterList = this.generateParameter();

        Docket docket = new Docket(DocumentationType.SWAGGER_2).groupName(groupName)
                .forCodeGeneration(true)
                .select()
                // 过滤规则
                .apis(this.getControllerPredicate(apiTagList))
                // 与 过滤规则 controller 包路径 二选一
                // .apis(RequestHandlerSelectors.basePackage(packAge))
                .paths(PathSelectors.any())
                .build().apiInfo(this.apiInfo())
                .globalOperationParameters(parameterList);
        if (StringUtils.isNotBlank(host)) {
            docket = docket.host(host);
        }
        return docket;
    }

    private Predicate<RequestHandler> getControllerPredicate(List<String> apiTagList) {
        Predicate<RequestHandler> methodPredicate = (input) -> {
            Api api = null;
            Optional<Api> apiOptional = input.findControllerAnnotation(Api.class);
            if (apiOptional.isPresent()) {
                api = apiOptional.get();
            }
            if (api == null) {
                return false;
            }
            List<String> tags = Arrays.asList(api.tags());
            if (apiTagList.containsAll(tags)) {
                return true;
            }
            return false;
        };
        Predicate controllerPredicate = Predicates.or(RequestHandlerSelectors.withClassAnnotation(RestController.class), RequestHandlerSelectors.withClassAnnotation(Controller.class));
        return Predicates.and(controllerPredicate, methodPredicate);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(title)
                .description(description)
                .version(version)
                .termsOfServiceUrl(teamUrl)
                .contact(new Contact("1024lab", teamUrl, "1024lab@sina.com"))
                .build();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    /**
     * 生成共用请求参数
     *
     * @return
     */
    private List<Parameter> generateParameter() {
        // 配置全局参数 token
        Parameter token = new ParameterBuilder().name(RequestHeaderConst.TOKEN)
                .description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header").defaultValue("1")
                .required(false)
                .build();
        return Lists.newArrayList(token);
    }
}
