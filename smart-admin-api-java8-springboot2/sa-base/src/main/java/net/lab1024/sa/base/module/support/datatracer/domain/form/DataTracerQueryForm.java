package net.lab1024.sa.base.module.support.datatracer.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * 查询表单
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-07-23 19:38:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class DataTracerQueryForm extends PageParam {

    @SchemaEnum(DataTracerTypeEnum.class)
    private Integer type;

    @Schema(description = "业务id")
    @NotNull(message = "业务id不能为空")
    private Long dataId;

    @Schema(description = "关键字")
    private String keywords;
}
