package net.lab1024.smartadmin.module.business.peony.domain.dto;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

/**
 * 新建 [ 牡丹花 ]
 *
 * @author 卓大
 * @version 1.0
 * @company 1024创新实验室( www.1024lab.net )
 * @copyright (c) 2018 1024创新实验室( www.1024lab.net )Inc. All rights reserved.
 * @date  2020-04-06 18:17:56
 * @since JDK1.8
 */
@Data
public class PeonyAddDTO {
    @ApiModelProperty("品种")
    private String kind;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("图片链接")
    private String imageUrl;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}
