package net.lab1024.sa.admin.module.business.ai.model.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模型配置表 分页查询表单
 *
 * @Author Mission
 * @Date 2026-01-28 09:59:38
 * @Copyright Mission
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class AiModelConfigQueryForm extends PageParam {

    @Schema(description = "模型名称")
    private String modelName;

    @Schema(description = "厂商标识")
    private String provider;

}
