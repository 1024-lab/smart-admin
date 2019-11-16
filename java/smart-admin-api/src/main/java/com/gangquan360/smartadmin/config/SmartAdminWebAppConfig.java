package com.gangquan360.smartadmin.config;

import com.gangquan360.smartadmin.interceptor.SmartAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @Description
 * @Author lihaifan
 * @Date Created in 2017/10/24 13:48
 */
@Configuration
public class SmartAdminWebAppConfig implements WebMvcConfigurer{
    @Autowired
    private SmartAuthenticationInterceptor smartAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(smartAuthenticationInterceptor).addPathPatterns("/**");
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/druidMonitor").setViewName("redirect:/druid/index.html");
        registry.addViewController("/swaggerApi").setViewName("redirect:/swagger-ui.html");
    }
}
