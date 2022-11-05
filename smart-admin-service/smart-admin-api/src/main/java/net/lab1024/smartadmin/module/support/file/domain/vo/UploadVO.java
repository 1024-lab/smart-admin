package net.lab1024.smartadmin.module.support.file.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2018/12/11 0011 上午 10:57
 * @since JDK1.8
 */
@Data
public class UploadVO {

    @ApiModelProperty(value = "文件名称")
    private String fileName;
    @ApiModelProperty(value = "url")
    private String url;
    @ApiModelProperty(value = "filePath")
    private String filePath;
    @ApiModelProperty(value = "文件大小")
    private Long fileSize;
}
