package net.lab1024.sa.admin.module.business.knowledge.controller;

import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiKnowledgeAddForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiKnowledgeQueryForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiKnowledgeUpdateForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.vo.AiKnowledgeVO;
import net.lab1024.sa.admin.module.business.knowledge.service.AiKnowledgeService;
import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * 知识库 Controller
 *
 * @Author Mission
 * @Date 2026-01-28 10:24:07
 * @Copyright Mission
 */

@RestController
@Tag(name = "知识库")
public class AiKnowledgeController {

    @Resource
    private AiKnowledgeService aiKnowledgeService;

    @Operation(summary = "分页查询 @author Mission")
    @PostMapping("/aiKnowledge/queryPage")
    @SaCheckPermission("aiKnowledge:query")
    public ResponseDTO<PageResult<AiKnowledgeVO>> queryPage(@RequestBody @Valid AiKnowledgeQueryForm queryForm) {
        return ResponseDTO.ok(aiKnowledgeService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author Mission")
    @PostMapping("/aiKnowledge/add")
    @SaCheckPermission("aiKnowledge:add")
    public ResponseDTO<String> add(@RequestBody @Valid AiKnowledgeAddForm addForm) {
        return aiKnowledgeService.add(addForm);
    }

    @Operation(summary = "更新 @author Mission")
    @PostMapping("/aiKnowledge/update")
    @SaCheckPermission("aiKnowledge:update")
    public ResponseDTO<String> update(@RequestBody @Valid AiKnowledgeUpdateForm updateForm) {
        return aiKnowledgeService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author Mission")
    @PostMapping("/aiKnowledge/batchDelete")
    @SaCheckPermission("aiKnowledge:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<String> idList) {
        return aiKnowledgeService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author Mission")
    @GetMapping("/aiKnowledge/delete/{id}")
    @SaCheckPermission("aiKnowledge:delete")
    public ResponseDTO<String> batchDelete(@PathVariable String id) {
        return aiKnowledgeService.delete(id);
    }
}
