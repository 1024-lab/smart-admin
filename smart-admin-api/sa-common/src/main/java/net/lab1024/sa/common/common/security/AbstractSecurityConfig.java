package net.lab1024.sa.common.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Spring Security
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021/8/3 17:50
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
public abstract class AbstractSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CorsFilter corsFilter;

    @Autowired
    private List<String> noNeedLoginUrlList;

    @Autowired
    private List<String> ignoreUrlList;

    /**
     * Token获取用户信息
     *
     * @return
     */
    protected abstract BiFunction<String, HttpServletRequest, UserDetails> userFunction();

    /**
     * 需要认证的url集合
     *
     * @return
     */
    protected abstract String[] getAuthenticatedUrlPatterns();

    /**
     * 不需要登录的url集合
     *
     * @return
     */
    protected String[] getNoNeedLoginUrl() {
        return noNeedLoginUrlList.toArray(new String[noNeedLoginUrlList.size()]);
    }

    /**
     * 忽略的url集合
     *
     * @return
     */
    protected String[] getIgnoreUrlList() {
        return ignoreUrlList.toArray(new String[ignoreUrlList.size()]);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(new SecurityAuthenticationFailHandler()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                //忽略的url
                .antMatchers(this.getIgnoreUrlList()).permitAll()
                // 不需要登陆的url
                .antMatchers(this.getNoNeedLoginUrl()).permitAll()
                //需要校验权限的url
                .antMatchers(getAuthenticatedUrlPatterns()).authenticated();

        // token filter 进行校验
        httpSecurity.addFilterBefore(new SecurityTokenFilter(this.userFunction()), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(corsFilter, SecurityTokenFilter.class);
        // 禁用spring security 使用 X-Frame-Options防止网页被Frame
        httpSecurity.headers().frameOptions().disable();

    }


}
