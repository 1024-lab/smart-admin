package net.lab1024.sa.common.module.support.dict.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 字典
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/5/26 19:40:55
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class DictKeyUpdateForm extends DictKeyAddForm {

    @ApiModelProperty("keyId")
    @NotNull(message = "keyId不能为空")
    private Long dictKeyId;
}