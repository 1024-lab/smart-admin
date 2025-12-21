package net.lab1024.sa.admin.module.business.ai.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ai应用管理表 分页查询表单
 *
 * @Author Mission
 * @Date 2025-12-21 16:00:44
 * @Copyright Mission
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class AiAppQueryForm extends PageParam {

    @Schema(description = "关键字查询")
    private String keywords;

}
