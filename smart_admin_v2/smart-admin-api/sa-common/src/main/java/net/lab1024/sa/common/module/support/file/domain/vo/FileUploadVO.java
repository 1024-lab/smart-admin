package net.lab1024.sa.common.module.support.file.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件信息
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2019年10月11日 15:34:47
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class FileUploadVO {

    @ApiModelProperty(value = "文件id")
    private Long fileId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "fileUrl")
    private String fileUrl;

    @ApiModelProperty(value = "fileKey")
    private String fileKey;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "文件类型")
    private String fileType;
}
