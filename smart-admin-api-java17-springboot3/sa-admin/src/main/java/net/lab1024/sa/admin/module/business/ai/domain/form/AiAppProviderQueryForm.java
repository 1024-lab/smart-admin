package net.lab1024.sa.admin.module.business.ai.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用提供商 分页查询表单
 *
 * @Author Mission
 * @Date 2025-12-21 16:06:19
 * @Copyright Mission
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class AiAppProviderQueryForm extends PageParam {

    @Schema(description = "名称")
    private String name;

}
