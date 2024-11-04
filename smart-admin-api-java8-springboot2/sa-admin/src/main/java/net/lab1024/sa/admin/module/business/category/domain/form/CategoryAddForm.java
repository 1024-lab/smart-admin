package net.lab1024.sa.admin.module.business.category.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.admin.module.business.category.constant.CategoryTypeEnum;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 类目 添加
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021/08/05 21:26:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class CategoryAddForm {

    @Schema(description = "类目名称", required = true)
    @NotBlank(message = "类目名称不能为空")
    @Length(max = 20, message = "类目名称最多20字符")
    private String categoryName;

    @SchemaEnum(desc = "分类类型", value = CategoryTypeEnum.class)
    @CheckEnum(value = CategoryTypeEnum.class, required = true, message = "分类错误")
    private Integer categoryType;

    @Schema(description = "父级类目id|可选")
    private Long parentId;

    @Schema(description = "排序|可选")
    private Integer sort;

    @Schema(description = "备注|可选")
    @Length(max = 200, message = "备注最多200字符")
    private String remark;

    @Schema(description = "禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

}
