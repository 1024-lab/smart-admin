package net.lab1024.sa.admin.module.business.category.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.admin.module.business.category.constant.CategoryTypeEnum;
import net.lab1024.sa.base.common.swagger.SchemaEnum;

import java.time.LocalDateTime;

/**
 * 类目
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021/08/05 21:26:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class CategoryVO {

    @Schema(description = "类目名称", required = true)
    private String categoryName;

    @SchemaEnum(desc = "分类类型", value = CategoryTypeEnum.class)
    private Integer categoryType;

    @Schema(description = "父级类目id|可选")
    private Long parentId;

    @Schema(description = "排序|可选")
    private Integer sort;

    @Schema(description = "备注|可选")
    private String remark;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "类目id")
    private Long categoryId;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
