package com.gangquan360.smartadmin.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * description
 *
 * @author listen
 * @date 2017/12/19 13:54
 */
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = {"com.gangquan360.smartadmin.module.*"})
public class SmartMybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    @Conditional(SystemEnvironmentCondition.class)
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

}
