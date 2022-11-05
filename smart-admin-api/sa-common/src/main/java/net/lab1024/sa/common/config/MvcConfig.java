package net.lab1024.sa.common.config;

import net.lab1024.sa.common.common.interceptor.AbstractInterceptor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web相关配置
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021-09-02 20:21:10
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired(required = false)
    private List<HandlerInterceptor> interceptorList;

    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        if (CollectionUtils.isEmpty(interceptorList)) {
            return;
        }
        interceptorList.forEach(e->{
            registry.addInterceptor(e).addPathPatterns("/**");
        });
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/preview/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/druidMonitor").setViewName("redirect:druid/index.html");
        registry.addViewController("/swaggerApi").setViewName("redirect:swagger-ui.html");
    }
}
