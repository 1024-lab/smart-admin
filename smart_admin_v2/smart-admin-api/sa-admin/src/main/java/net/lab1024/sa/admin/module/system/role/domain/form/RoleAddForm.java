package net.lab1024.sa.admin.module.system.role.domain.form;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 角色 添加表单
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2022-02-26 19:09:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class RoleAddForm {

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    @NotNull(message = "角色名称不能为空")
    @Length(min = 1, max = 20, message = "角色名称(1-20)个字符")
    private String roleName;

    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    @Length(max = 255, message = "角色描述最多255个字符")
    private String remark;


}
