package net.lab1024.smartadmin.module.system.position.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zzr
 */
@Data
public class PositionResultVO {

    @ApiModelProperty("主键")
    private Long id;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位名称")
    private String positionName;

    /**
     * 岗位描述
     */
    @ApiModelProperty("岗位描述")
    private String remark;

}
