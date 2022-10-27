package net.lab1024.sa.common.module.support.dict.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
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
public class DictValueAddForm {

    @ApiModelProperty("dictKeyId")
    @NotNull(message = "dictKeyId不能为空")
    private Long dictKeyId;

    @ApiModelProperty("编码")
    @NotBlank(message = "编码不能为空")
    @Length(max = 50,message = "编码太长了，不能超过50字符")
    private String valueCode;

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    @Length(max = 50,message = "名称太长了，不能超过50字符")
    private String valueName;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty("备注")
    @Length(max = 500,message = "备注太长了")
    private String remark;

}