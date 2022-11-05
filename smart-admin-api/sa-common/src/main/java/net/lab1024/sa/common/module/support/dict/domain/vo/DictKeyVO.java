package net.lab1024.sa.common.module.support.dict.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class DictKeyVO {

    @ApiModelProperty("dictKeyId")
    private Long dictKeyId;

    @ApiModelProperty("编码")
    private String keyCode;

    @ApiModelProperty("名称")
    private String keyName;

    @ApiModelProperty("备注")
    private String remark;
}