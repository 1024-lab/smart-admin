package net.lab1024.sa.common.common.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.common.common.enumeration.SystemEnvironmentEnum;

/**
 * 系统环境
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021/8/13 21:06:11
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@AllArgsConstructor
@Getter
public class SystemEnvironment {

    /**
     * 是否位生产环境
     */
    private boolean isProd;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 当前环境
     */
    private SystemEnvironmentEnum currentEnvironment;
}
