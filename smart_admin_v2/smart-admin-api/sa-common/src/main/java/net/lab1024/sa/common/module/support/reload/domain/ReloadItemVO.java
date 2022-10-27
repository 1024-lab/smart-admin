package net.lab1024.sa.common.module.support.reload.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

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
public class ReloadItemVO {

    @ApiModelProperty("加载项标签")
    private String tag;

    @ApiModelProperty("参数")
    private String args;

    @ApiModelProperty("运行标识")
    private String identification;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}
