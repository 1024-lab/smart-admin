package net.lab1024.sa.admin.module.business.knowledge.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 知识库 分页查询表单
 *
 * @Author Mission
 * @Date 2026-01-28 10:24:07
 * @Copyright Mission
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class AiKnowledgeQueryForm extends PageParam {

    @Schema(description = "知识库名称")
    private String name;

}
