package net.lab1024.sa.common.module.support.codegenerator.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.domain.PageParam;


/**
 * 查询表数据
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-30 22:15:38
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class TableQueryForm extends PageParam {

    @ApiModelProperty("表名关键字")
    private String tableNameKeywords;

}
