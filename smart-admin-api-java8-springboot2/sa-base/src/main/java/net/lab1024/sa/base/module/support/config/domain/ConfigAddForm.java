package net.lab1024.sa.base.module.support.config.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 添加配置表单
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-14 20:46:27
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class ConfigAddForm {

    @Schema(description = "参数key")
    @NotBlank(message = "参数key不能为空")
    @Length(max = 255, message = "参数key最多255个字符")
    private String configKey;

    @Schema(description = "参数的值")
    @NotBlank(message = "参数的值不能为空")
    @Length(max = 60000, message = "参数的值最多60000个字符")
    private String configValue;

    @Schema(description = "参数名称")
    @NotBlank(message = "参数名称不能为空")
    @Length(max = 255, message = "参数名称最多255个字符")
    private String configName;

    @Schema(description = "备注")
    @Length(max = 255, message = "备注最多255个字符")
    private String remark;
}
