package net.lab1024.smartadmin.config;

import net.lab1024.smartadmin.interceptor.SmartAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class SmartAdminWebAppConfig implements WebMvcConfigurer {

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
