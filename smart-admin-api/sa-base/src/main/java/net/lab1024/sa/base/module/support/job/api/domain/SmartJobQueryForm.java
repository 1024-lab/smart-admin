package net.lab1024.sa.base.module.support.job.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.base.module.support.job.constant.SmartJobTriggerTypeEnum;
import org.hibernate.validator.constraints.Length;

/**
 * 定时任务 分页查询
 *
 * @author huke
 * @date 2024/6/17 20:50
 */
@Data
public class SmartJobQueryForm extends PageParam {

    @Schema(description = "搜索词|可选")
    @Length(max = 50, message = "搜索词最多50字符")
    private String searchWord;

    @SchemaEnum(desc = "触发类型", value = SmartJobTriggerTypeEnum.class)
    @CheckEnum(value = SmartJobTriggerTypeEnum.class, message = "触发类型错误")
    private String triggerType;

    @Schema(description = "是否启用|可选")
    private Boolean enabledFlag;
}
