
package net.lab1024.sa.base.module.support.codegenerator.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 列
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/9/21 21:07:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */

@Data
public class TableColumnVO {

    @Schema(description = "列名")
    private String columnName;

    @Schema(description = "列描述")
    private String columnComment;

    @Schema(description = "columnKey")
    private String columnKey;

    @Schema(description = "extra")
    private String extra;

    @Schema(description = "是否为空")
    private String isNullable;

    @Schema(description = "数据类型varchar")
    private String dataType;

    @Schema(description = "列类型varchar(50)")
    private String columnType;


}
