package net.lab1024.sa.common.module.support.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.common.common.enumeration.BaseEnum;

/**
 * 系统配置常量类
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-14 20:46:27
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Getter
@AllArgsConstructor
public enum ConfigKeyEnum implements BaseEnum {

    /**
     * 本地上传路径前缀
     */
    LOCAL_UPLOAD_URL_PREFIX("local_upload_url_prefix", "本地上传路径前缀"),

    /**
     * 万能密码
     */
    SUPER_PASSWORD("super_password", "万能密码"),

    ;

    private final String value;

    private final String desc;
}
