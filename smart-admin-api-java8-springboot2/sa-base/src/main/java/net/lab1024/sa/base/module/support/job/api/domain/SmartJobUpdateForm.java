package net.lab1024.sa.base.module.support.job.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.base.module.support.job.constant.SmartJobTriggerTypeEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 定时任务 更新
 *
 * @author huke
 * @date 2024/6/17 21:30
 */
@Data
public class SmartJobUpdateForm {

    @Schema(description = "任务id")
    @NotNull(message = "任务id不能为空")
    private Integer jobId;

    @Schema(description = "任务名称")
    @NotBlank(message = "任务名称不能为空")
    @Length(max = 100, message = "任务名称最多100字符")
    private String jobName;

    @Schema(description = "任务执行类")
    @NotBlank(message = "任务执行类不能为空")
    @Length(max = 200, message = "任务执行类最多200字符")
    private String jobClass;

    @SchemaEnum(desc = "触发类型", value = SmartJobTriggerTypeEnum.class)
    @CheckEnum(value = SmartJobTriggerTypeEnum.class, required = true, message = "触发类型错误")
    private String triggerType;

    @Schema(description = "触发配置")
    @NotBlank(message = "触发配置不能为空")
    @Length(max = 100, message = "触发配置最多100字符")
    private String triggerValue;

    @Schema(description = "定时任务参数|可选")
    @Length(max = 1000, message = "定时任务参数最多1000字符")
    private String param;

    @Schema(description = "是否开启")
    @NotNull(message = "是否开启不能为空")
    private Boolean enabledFlag;

    @Schema(description = "备注")
    @Length(max = 250, message = "任务备注最多250字符")
    private String remark;

    @NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    private Integer sort;

    @Schema(hidden = true)
    private String updateName;
}
