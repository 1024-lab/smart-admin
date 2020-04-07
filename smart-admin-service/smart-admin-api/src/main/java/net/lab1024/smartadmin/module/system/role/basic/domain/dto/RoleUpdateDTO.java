package net.lab1024.smartadmin.module.system.role.basic.domain.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 角色更新修改DTO
 *
 * @author listen
 * @date 2017/12/28 09:40
 */
@Data
public class RoleUpdateDTO extends RoleAddDTO {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能为空")
    protected Long id;


}
