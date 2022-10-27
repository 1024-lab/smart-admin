package net.lab1024.sa.common.module.support.config.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 配置更新表单
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-14 20:46:27
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class ConfigUpdateForm extends ConfigAddForm {

    @ApiModelProperty("configId")
    @NotNull(message = "configId不能为空")
    private Long configId;
}
