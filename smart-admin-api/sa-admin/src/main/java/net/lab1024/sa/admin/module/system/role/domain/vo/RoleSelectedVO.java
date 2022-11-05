package net.lab1024.sa.admin.module.system.role.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 选择角色
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-04-08 21:53:04
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class RoleSelectedVO extends RoleVO {

    @ApiModelProperty("角色名称")
    private Boolean selected;
}
