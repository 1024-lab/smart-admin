
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

    @Schema(description = "数据类型varchar")
    private String dataType;

    @Schema(description = "是否为空")
    private Boolean nullableFlag;

    @Schema(description = "是否为主键")
    private Boolean primaryKeyFlag;

    @Schema(description = "是否递增")
    private Boolean autoIncreaseFlag;

    // --------------- 临时字段 -------------------

    @Schema(hidden = true)
    private String columnKey;

    @Schema(hidden = true)
    private String extra;

    @Schema(hidden = true)
    private String isNullable;
}
