package net.lab1024.sa.admin.module.system.role.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.domain.PageParam;

/**
 * 角色的员工查询
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-04-08 21:53:04
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class RoleEmployeeQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("角色id")
    private String roleId;
}
