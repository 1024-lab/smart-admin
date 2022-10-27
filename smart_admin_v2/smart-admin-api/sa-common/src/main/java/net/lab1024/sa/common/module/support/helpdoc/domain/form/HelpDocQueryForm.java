package net.lab1024.sa.common.module.support.helpdoc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 帮助文档 分页查询
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-20 23:11:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class HelpDocQueryForm extends PageParam {

    @ApiModelProperty("分类")
    private Long helpDocCatalogId;

    @ApiModelProperty("标题")
    private String keywords;

    @ApiModelProperty("创建-开始时间")
    private LocalDate createTimeBegin;

    @ApiModelProperty("创建-截止时间")
    private LocalDate createTimeEnd;

}
