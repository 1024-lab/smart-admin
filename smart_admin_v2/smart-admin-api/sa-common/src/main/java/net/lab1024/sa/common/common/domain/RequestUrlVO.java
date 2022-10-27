package net.lab1024.sa.common.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请求url返回对象
 *
 * @Author 1024创新实验室: 李善逸
 * @Date 2021/9/1 20:15
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class RequestUrlVO {

    @ApiModelProperty("注释说明")
    private String comment;

    @ApiModelProperty("controller.method")
    private String name;

    @ApiModelProperty("url")
    private String url;
}
