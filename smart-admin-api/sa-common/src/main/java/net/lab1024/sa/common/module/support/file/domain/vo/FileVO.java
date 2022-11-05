package net.lab1024.sa.common.module.support.file.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.enumeration.UserTypeEnum;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.sa.common.module.support.file.constant.FileFolderTypeEnum;

import java.time.LocalDateTime;

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
public class FileVO {

    @ApiModelProperty("主键")
    private Long fileId;

    @ApiModelProperty("存储文件夹类型")
    @ApiModelPropertyEnum(FileFolderTypeEnum.class)
    private Integer folderType;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件大小")
    private Integer fileSize;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("文件路径")
    private String fileKey;

    @ApiModelProperty("上传人")
    private Long creatorId;

    @ApiModelProperty("上传人")
    private String creatorName;

    @ApiModelPropertyEnum(value = UserTypeEnum.class, desc = "创建人类型")
    private Integer creatorUserType;

    @ApiModelProperty("文件展示url")
    private String fileUrl;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
