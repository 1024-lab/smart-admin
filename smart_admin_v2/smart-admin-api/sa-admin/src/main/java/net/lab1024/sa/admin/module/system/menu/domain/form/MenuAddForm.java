package net.lab1024.sa.admin.module.system.menu.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 菜单 添加表单
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-03-06 22:04:37
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class MenuAddForm extends MenuBaseForm {

    @ApiModelProperty(hidden = true)
    private Long createUserId;
}
