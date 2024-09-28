
package net.lab1024.sa.base.module.support.codegenerator.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.module.support.codegenerator.domain.model.*;

import java.util.List;

/**
 * 表的配置信息
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/9/21 21:07:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */

@Data
public class TableConfigVO {

    @Schema(description = "基础命名信息")
    private CodeBasic basic;

    @Schema(description = "字段列")
    private List<CodeField> fields;

    @Schema(description = "增加、修改 信息")
    private CodeInsertAndUpdate insertAndUpdate;

    @Schema(description = "删除 信息")
    private CodeDelete deleteInfo;

    @Schema(description = "查询字段")
    private List<CodeQueryField> queryFields;

    @Schema(description = "列表字段")
    private List<CodeTableField> tableFields;
}
