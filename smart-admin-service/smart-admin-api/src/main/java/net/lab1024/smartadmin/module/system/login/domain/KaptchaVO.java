package net.lab1024.smartadmin.module.system.login.domain;

import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/7/4 0004 上午 10:11
 * @since JDK1.8
 */
@Data
public class KaptchaVO {

    /**
     *  验证码UUID
     */
    private String uuid;

    /**
     * base64 验证码
     */
    private String code;

}
