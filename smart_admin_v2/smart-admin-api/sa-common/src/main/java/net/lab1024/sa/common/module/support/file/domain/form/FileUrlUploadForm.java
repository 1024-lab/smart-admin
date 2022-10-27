package net.lab1024.sa.common.module.support.file.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.sa.common.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.common.module.support.file.constant.FileFolderTypeEnum;

import javax.validation.constraints.NotBlank;

/**
 * url上传文件
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2019年10月11日 15:34:47
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class FileUrlUploadForm {

    @ApiModelPropertyEnum(value = FileFolderTypeEnum.class, desc = "业务类型")
    @CheckEnum(value = FileFolderTypeEnum.class, required = true, message = "业务类型错误")
    private Integer folder;

    @ApiModelProperty("文件url")
    @NotBlank(message = "文件url不能为空")
    private String fileUrl;

}
