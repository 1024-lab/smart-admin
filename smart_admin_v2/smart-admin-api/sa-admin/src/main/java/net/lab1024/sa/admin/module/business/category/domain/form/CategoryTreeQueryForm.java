package net.lab1024.sa.admin.module.business.category.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.admin.module.business.category.constant.CategoryTypeEnum;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;

/**
 * 类目 层级树查询
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021/08/05 21:26:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Data
public class CategoryTreeQueryForm {

    @ApiModelPropertyEnum(desc = "分类类型|可选", value = CategoryTypeEnum.class)
    private Integer categoryType;

    @ApiModelProperty("父级类目id|可选")
    private Long parentId;
}
