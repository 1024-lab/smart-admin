package net.lab1024.sa.common.module.support.jwe;

import lombok.Data;

/**
 * 解密用用户信息作为key
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020/11/25 20:46
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class JweUserKey {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 扩展信息
     */
    private String extData;

}
