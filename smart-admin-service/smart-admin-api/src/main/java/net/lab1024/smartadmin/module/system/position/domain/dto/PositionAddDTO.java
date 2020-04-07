package net.lab1024.smartadmin.module.system.position.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 岗位
 *
 * @author zzr
 */
@Data
public class PositionAddDTO {

    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位名称")
    @NotBlank(message = "岗位名称不能为空")
    private String positionName;

    /**
     * 岗位描述
     */
    @ApiModelProperty("岗位描述")
    private String remark;
}
