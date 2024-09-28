package net.lab1024.sa.admin.module.business.category.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 类目 层级树 vo
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021/08/05 21:26:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class CategoryTreeVO {

    @Schema(description = "类目id")
    private Long categoryId;

    @Schema(description = "类目名称")
    private String categoryName;

    @Schema(description = "类目层级全称")
    private String categoryFullName;

    @Schema(description = "父级id")
    private Long parentId;

    @Schema(description = "类目id")
    private Long value;

    @Schema(description = "类目名称")
    private String label;

    @Schema(description = "子类")
    private List<CategoryTreeVO> children;
}
