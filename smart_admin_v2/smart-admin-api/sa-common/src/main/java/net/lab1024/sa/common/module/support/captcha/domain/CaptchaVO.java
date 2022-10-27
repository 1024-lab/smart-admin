package net.lab1024.sa.common.module.support.captcha.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 图形验证码 VO
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021/8/31 20:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class CaptchaVO {

    @ApiModelProperty("验证码唯一标识")
    private String captchaUuid;

    @ApiModelProperty("验证码图片内容-生产环境无效")
    private String captchaText;

    @ApiModelProperty("验证码Base64图片")
    private String captchaBase64Image;

    @ApiModelProperty("过期时间（秒）")
    private Long expireSeconds;
}
