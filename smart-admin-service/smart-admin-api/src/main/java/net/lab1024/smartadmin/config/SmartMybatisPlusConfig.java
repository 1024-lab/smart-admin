package net.lab1024.smartadmin.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import net.lab1024.smartadmin.common.mybatis.MyBatisSqlQuerySqlDebugPlugin;
import net.lab1024.smartadmin.common.mybatis.MyBatisSqlUpdateSqlDebugPlugin;
import net.lab1024.smartadmin.constant.SystemEnvironmentEnum;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zhuoda
 */
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = {"net.lab1024.smartadmin.module.*"})
public class SmartMybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        // 开启 count 的 join 优化,只针对 left join !!!
        return new PaginationInterceptor().setCountSqlParser(new JsqlParserCountOptimize(true));
    }

    /**
     * 打印sql log
     * @return
     */
    @Bean
    @Profile(SystemEnvironmentEnum.DEV_ENV)
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.addInterceptor(new MyBatisSqlUpdateSqlDebugPlugin());
            configuration.addInterceptor(new MyBatisSqlQuerySqlDebugPlugin());
        };
    }
}
