package net.lab1024.sa.admin;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SmartAdmin 项目启动类
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022-08-29 21:00:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan(basePackages = {"net.lab1024.sa"})
@MapperScan(value = "net.lab1024.sa", annotationClass = Mapper.class)
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class })
public class SmartAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartAdminApplication.class, args);
    }
}
