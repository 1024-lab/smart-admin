package net.lab1024.sa.admin.module.system.role.domain.form;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 角色更新修改
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2022-02-26 19:09:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class RoleUpdateForm extends RoleAddForm {

    /**
     * 角色id
     */
    @Schema(description = "角色id")
    @NotNull(message = "角色id不能为空")
    protected Long roleId;


}
