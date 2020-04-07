package net.lab1024.smartadmin.module.system.systemconfig.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@Data
public class SystemConfigAddDTO {

    @ApiModelProperty("参数key")
    @NotBlank(message = "参数key不能为空")
    @Length(max = 255, message = "参数key最多255个字符")
    private String configKey;

    @ApiModelProperty("参数的值")
    @NotBlank(message = "参数的值不能为空")
    @Length(max = 65530, message = "参数的值最多65530个字符")
    private String configValue;

    @ApiModelProperty("参数名称")
    @NotBlank(message = "参数名称不能为空")
    @Length(max = 255, message = "参数名称最多255个字符")
    private String configName;

    @ApiModelProperty("参数类别")
    @NotBlank(message = "参数类别不能为空")
    @Length(max = 255, message = "参数类别最多255个字符")
    private String configGroup;

    @ApiModelProperty("备注")
    @Length(max = 255, message = "备注最多255个字符")
    private String remark;
}
