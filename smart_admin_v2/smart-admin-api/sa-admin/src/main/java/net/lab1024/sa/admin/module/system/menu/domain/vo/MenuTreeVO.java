package net.lab1024.sa.admin.module.system.menu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
public class MenuTreeVO extends MenuVO{

    @ApiModelProperty("菜单子集")
    private List<MenuTreeVO> children;
}
