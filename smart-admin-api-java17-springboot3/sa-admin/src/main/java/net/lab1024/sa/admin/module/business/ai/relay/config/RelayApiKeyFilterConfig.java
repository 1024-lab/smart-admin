package net.lab1024.sa.admin.module.business.ai.relay.config;

import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayTokenDao;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayUserAccountDao;
import net.lab1024.sa.admin.module.business.ai.relay.filter.RelayApiKeyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;

@Configuration
public class RelayApiKeyFilterConfig {

    @Resource
    private RelayTokenDao relayTokenDao;

    @Resource
    private RelayUserAccountDao relayUserAccountDao;

    @Bean
    public FilterRegistrationBean<RelayApiKeyFilter> relayApiKeyFilterRegistration() {
        FilterRegistrationBean<RelayApiKeyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RelayApiKeyFilter(relayTokenDao, relayUserAccountDao));
        registration.addUrlPatterns("/v1/*");
        registration.setOrder(1);
        registration.setName("relayApiKeyFilter");
        return registration;
    }
}
