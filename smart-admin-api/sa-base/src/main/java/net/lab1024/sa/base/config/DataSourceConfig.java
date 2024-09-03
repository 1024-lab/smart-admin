package net.lab1024.sa.base.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.domain.DataScopePlugin;
import net.lab1024.sa.base.handler.MybatisPlusFillHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源配置
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2017-11-28 15:21:10
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.driver-class-name}")
    String driver;

    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    @Value("${spring.datasource.initial-size}")
    int initialSize;

    @Value("${spring.datasource.min-idle}")
    int minIdle;

    @Value("${spring.datasource.max-active}")
    int maxActive;

    @Value("${spring.datasource.max-wait}")
    long maxWait;

    @Value("${spring.datasource.time-between-eviction-runs-millis}")
    long timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.min-evictable-idle-time-millis}")
    long minEvictableIdleTimeMillis;

    @Value("${spring.datasource.filters}")
    String filters;

    @Value("${spring.datasource.druid.username}")
    String druidUserName;

    @Value("${spring.datasource.druid.password}")
    String druidPassword;

    @Value("${spring.datasource.druid.login.enabled}")
    boolean druidLoginEnable;

    @Value("${spring.datasource.druid.method.pointcut}")
    String methodPointcut;

    @javax.annotation.Resource
    private MybatisPlusInterceptor paginationInterceptor;

    @javax.annotation.Resource
    private DataScopePlugin dataScopePlugin;

    @Bean
    @Primary
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDbType(DbType.MYSQL.getDb());
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setValidationQuery("SELECT 1");
        try {
            druidDataSource.setFilters(filters);
            ArrayList<Filter> arrayList = new ArrayList<>();
            StatFilter statFilter = new StatFilter();
            statFilter.setMergeSql(true);
            statFilter.setSlowSqlMillis(1000);
            statFilter.setLogSlowSql(true);
            arrayList.add(statFilter);
            druidDataSource.setProxyFilters(arrayList);
            druidDataSource.init();
        } catch (SQLException e) {
            log.error("初始化数据源出错", e);
        }

        return druidDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(druidDataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:/mapper/**/*.xml");
        factoryBean.setMapperLocations(resources);

        // 设置 MyBatis-Plus 分页插件 注意此处myBatisPlugin一定要放在后面
        List<Interceptor> pluginsList = new ArrayList<>();
        pluginsList.add(paginationInterceptor);
        if (dataScopePlugin != null) {
            pluginsList.add(dataScopePlugin);
        }
        factoryBean.setPlugins(pluginsList.toArray(new Interceptor[pluginsList.size()]));
        // 添加字段自动填充处理
        factoryBean.setGlobalConfig(new GlobalConfig().setBanner(false).setMetaObjectHandler(new MybatisPlusFillHandler()));

        return factoryBean.getObject();
    }

    /**
     * 非正式环境 才加载
     *
     * @return
     */
    @Conditional(SystemEnvironmentConfig.class)
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidServlet() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<String, String>();
        //不设置用户名密码可以直接通过druid/index.html访问
        if (druidLoginEnable) {
            initParameters.put("loginUsername", druidUserName);
            initParameters.put("loginPassword", druidPassword);
        }
        initParameters.put("resetEnable", "false");
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<WebStatFilter>();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/*");
        return filterRegistrationBean;
    }

    @Bean
    public JdkRegexpMethodPointcut jdkRegexpMethodPointcut() {
        JdkRegexpMethodPointcut jdkRegexpMethodPointcut = new JdkRegexpMethodPointcut();
        jdkRegexpMethodPointcut.setPatterns(methodPointcut);
        return jdkRegexpMethodPointcut;
    }

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor() {
        DefaultPointcutAdvisor pointcutAdvisor = new DefaultPointcutAdvisor();
        pointcutAdvisor.setPointcut(jdkRegexpMethodPointcut());
        pointcutAdvisor.setAdvice(new DruidStatInterceptor());
        return pointcutAdvisor;
    }

}
