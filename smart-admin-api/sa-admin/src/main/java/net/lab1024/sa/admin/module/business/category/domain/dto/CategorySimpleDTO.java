package net.lab1024.sa.admin.module.business.category.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类目 基础属性 DTO 类
 *
 * @author 胡克
 * @date 2021/1/20 16:17
 */
@Data
public class CategorySimpleDTO {

    @Schema(description = "类目id")
    private Long categoryId;

    @Schema(description = "类目名称")
    private String categoryName;

    @Schema(description = "类目层级全称")
    private String categoryFullName;

    @Schema(description = "父级id")
    private Long parentId;
}
