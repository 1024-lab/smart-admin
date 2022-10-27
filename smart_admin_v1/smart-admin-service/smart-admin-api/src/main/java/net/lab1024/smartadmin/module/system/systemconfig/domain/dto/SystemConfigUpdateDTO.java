package net.lab1024.smartadmin.module.system.systemconfig.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 
 * [  ]
 * 
 * @version 1.0
 * @since JDK1.8
 * @author yandanyang
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 */
@Data
public class SystemConfigUpdateDTO extends SystemConfigAddDTO{

    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Long id;
}
