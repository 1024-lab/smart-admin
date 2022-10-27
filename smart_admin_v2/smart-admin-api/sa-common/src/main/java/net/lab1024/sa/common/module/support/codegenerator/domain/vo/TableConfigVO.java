
package net.lab1024.sa.common.module.support.codegenerator.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.module.support.codegenerator.domain.model.*;

import java.util.List;

/**
 * 表的配置信息
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/9/21 21:07:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */

@Data
public class TableConfigVO {

    @ApiModelProperty("基础命名信息")
    private CodeBasic basic;

    @ApiModelProperty("字段列")
    private List<CodeField> fields;

    @ApiModelProperty("增加、修改 信息")
    private CodeInsertAndUpdate insertAndUpdate;

    @ApiModelProperty("删除 信息")
    private CodeDelete deleteInfo;

    @ApiModelProperty("查询字段")
    private List<CodeQueryField> queryFields;

    @ApiModelProperty("列表字段")
    private List<CodeTableField> tableFields;
}
