package net.lab1024.smartadmin.module.support.smartreload.domain.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * initDefines 项 DTO 类
 *
 * @author listen
 * @date 2018/02/10 09:29
 */
@Data
public class ReloadItemVO {

    /**
     * 加载项标签
     */
    @ApiModelProperty("加载项标签")
    private String tag;

    /**
     * 参数
     */
    @ApiModelProperty("参数")
    private String args;

    /**
     * 状态标识
     */
    @ApiModelProperty("状态标识")
    private String identification;

    /**
     * 更新时间
     */
    @ApiModelProperty("最后更新时间")
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

}
