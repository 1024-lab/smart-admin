package net.lab1024.sa.common.module.support.reload.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * reload (内存热加载、钩子等)
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2015-03-02 19:11:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class ReloadForm {

    @ApiModelProperty("标签")
    @NotBlank(message = "标签不能为空")
    private String tag;

    @ApiModelProperty("状态标识")
    @NotBlank(message = "状态标识不能为空")
    private String identification;

    @ApiModelProperty("参数")
    private String args;

}
