package net.lab1024.sa.common.module.support.config.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

/**
 * 分页查询 系统配置
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-14 20:46:27
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class ConfigQueryForm extends PageParam {

    @ApiModelProperty("参数KEY")
    @Length(max = 50, message = "参数Key最多50字符")
    private String configKey;
}
