package net.lab1024.smartadmin.module.system.role.basic.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/3/27 0027 下午 15:27
 * @since JDK1.8
 */
@Data
public class RoleSelectedVO extends RoleVO {

    @ApiModelProperty("角色名称")
    private Boolean selected;
}
