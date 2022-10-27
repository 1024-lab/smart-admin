package net.lab1024.sa.common.module.support.helpdoc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.domain.PageParam;

import javax.validation.constraints.NotNull;

/**
 * 查阅记录 查询
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-20 23:11:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class HelpDocViewRecordQueryForm extends PageParam {

    @ApiModelProperty("帮助文档id")
    @NotNull(message = "帮助文档id不能为空")
    private Long helpDocId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("关键字")
    private String keywords;


}
