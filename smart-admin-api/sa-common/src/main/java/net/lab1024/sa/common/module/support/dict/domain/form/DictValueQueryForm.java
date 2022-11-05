package net.lab1024.sa.common.module.support.dict.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.domain.PageParam;

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
public class DictValueQueryForm extends PageParam {

    @ApiModelProperty("dictKeyId")
    @NotNull(message = "dictKeyId不能为空")
    private Long dictKeyId;

    @ApiModelProperty("搜索词")
    private String searchWord;

    @ApiModelProperty(value = "删除标识",hidden = true)
    private Boolean deletedFlag;
}