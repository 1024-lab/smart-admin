package net.lab1024.sa.admin.module.business.category.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.admin.module.business.category.constant.CategoryTypeEnum;
import net.lab1024.sa.admin.module.business.category.domain.dto.CategoryBaseDTO;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.sa.common.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 类目
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021/08/05 21:26:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Data
public class CategoryVO {

    @ApiModelProperty(value = "类目名称", required = true)
    private String categoryName;

    @ApiModelPropertyEnum(desc = "分类类型", value = CategoryTypeEnum.class)
    private Integer categoryType;

    @ApiModelProperty("父级类目id|可选")
    private Long parentId;

    @ApiModelProperty("排序|可选")
    private Integer sort;

    @ApiModelProperty("备注|可选")
    private String remark;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty("类目id")
    private Long categoryId;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
