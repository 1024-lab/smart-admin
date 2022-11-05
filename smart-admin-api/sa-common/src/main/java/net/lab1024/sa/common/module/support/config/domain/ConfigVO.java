package net.lab1024.sa.common.module.support.config.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 配置信息
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-14 20:46:27
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class ConfigVO {
    @ApiModelProperty("主键")
    private Long configId;

    @ApiModelProperty("参数key")
    private String configKey;

    @ApiModelProperty("参数的值")
    private String configValue;

    @ApiModelProperty("参数名称")
    private String configName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("上次修改时间")
    private LocalDateTime updateTime;
}
