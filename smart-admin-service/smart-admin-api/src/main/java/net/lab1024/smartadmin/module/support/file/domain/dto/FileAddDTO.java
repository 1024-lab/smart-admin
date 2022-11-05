package net.lab1024.smartadmin.module.support.file.domain.dto;

import net.lab1024.smartadmin.common.anno.ApiModelPropertyEnum;
import net.lab1024.smartadmin.common.validator.en.CheckEnum;
import net.lab1024.smartadmin.module.support.file.constant.FileServiceTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
* @Description:    文件保存DTO
* @Author:         sbq
* @CreateDate:     2019/9/11 15:05
* @Version:        1.0
*/
@Data
public class FileAddDTO {

    @ApiModelProperty("相关业务id(无业务可写死一个id)")
    @NotBlank(message = "相关业务id不能为空")
    private String moduleId;

    @ApiModelProperty("相关业务类型(无模块写1)")
    @NotBlank(message = "相关业务类型不能为空")
    private String moduleType;

    @ApiModelPropertyEnum(enumDesc = "文件类型",value = FileServiceTypeEnum.class)
    @CheckEnum(enumClazz = FileServiceTypeEnum.class,message = "文件类型错误")
    private Integer fileLocationType;

    @ApiModelProperty("文件名称")
    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    @ApiModelProperty("文件路径")
    @NotBlank(message = "文件路径不能为空")
    private String filePath;
}
