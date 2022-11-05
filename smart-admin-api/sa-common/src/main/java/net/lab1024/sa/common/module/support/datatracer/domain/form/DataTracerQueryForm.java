package net.lab1024.sa.common.module.support.datatracer.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.domain.PageParam;
import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.sa.common.module.support.datatracer.constant.DataTracerTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * 查询表单
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-07-23 19:38:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class DataTracerQueryForm extends PageParam {

    @ApiModelPropertyEnum(DataTracerTypeEnum.class)
    private Integer type;

    @ApiModelProperty("业务id")
    @NotNull(message = "业务id不能为空")
    private Long dataId;

    @ApiModelProperty("关键字")
    private String keywords;
}
