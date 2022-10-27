package net.lab1024.sa.admin.module.system.role.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色的数据范围
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-04-08 21:53:04
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class RoleDataScopeVO {

    @ApiModelProperty("数据范围id")
    private Integer dataScopeType;

    @ApiModelProperty("可见范围")
    private Integer viewType;
}
