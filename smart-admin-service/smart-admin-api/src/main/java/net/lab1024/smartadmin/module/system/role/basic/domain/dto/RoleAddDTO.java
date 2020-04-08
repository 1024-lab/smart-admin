package net.lab1024.smartadmin.module.system.role.basic.domain.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 角色添加DTO
 *
 * @author listen
 * @date 2017/12/28 09:40
 */
@Data
public class RoleAddDTO {

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
