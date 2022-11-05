package net.lab1024.sa.admin.module.system.menu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.admin.module.system.menu.domain.form.MenuBaseForm;

import java.time.LocalDateTime;

/**
 * 菜单
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-03-06 22:04:37
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class MenuVO extends MenuBaseForm {

    @ApiModelProperty("菜单ID")
    private Long menuId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private Long createUserId;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("更新人")
    private Long updateUserId;
}
