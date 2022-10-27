package net.lab1024.sa.admin.module.business.category.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类目 基础属性 DTO 类
 *
 * @author 胡克
 * @date 2021/1/20 16:17
 */
@Data
public class CategorySimpleDTO {

    @ApiModelProperty("类目id")
    private Long categoryId;

    @ApiModelProperty("类目名称")
    private String categoryName;

    @ApiModelProperty("类目层级全称")
    private String categoryFullName;

    @ApiModelProperty("父级id")
    private Long parentId;
}
