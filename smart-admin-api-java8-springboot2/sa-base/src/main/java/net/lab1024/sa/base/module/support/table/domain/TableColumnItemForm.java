package net.lab1024.sa.base.module.support.table.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 自定义表格列
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 22:52:21
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class TableColumnItemForm {

    @NotEmpty(message = "列不能为空")
    @Schema(description = "字段")
    private String columnKey;

    @Schema(description = "宽度")
    private Integer width;

    @NotNull(message = "显示不能为空")
    @Schema(description = "是否显示")
    private Boolean showFlag;

    @NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    private Integer sort;


}
