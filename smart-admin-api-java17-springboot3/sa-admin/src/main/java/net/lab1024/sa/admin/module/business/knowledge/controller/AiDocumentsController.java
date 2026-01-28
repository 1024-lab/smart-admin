package net.lab1024.sa.admin.module.business.knowledge.controller;

import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiDocumentsQueryForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.vo.AiDocumentsVO;
import net.lab1024.sa.admin.module.business.knowledge.service.AiDocumentsService;
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
 * 文档列表 Controller
 *
 * @Author Mission
 * @Date 2026-01-28 20:36:21
 * @Copyright Mission
 */

@RestController
@Tag(name = "文档列表")
public class AiDocumentsController {

    @Resource
    private AiDocumentsService aiDocumentsService;

    @Operation(summary = "分页查询 @author Mission")
    @PostMapping("/aiDocuments/queryPage")
    @SaCheckPermission("aiDocuments:query")
    public ResponseDTO<PageResult<AiDocumentsVO>> queryPage(@RequestBody @Valid AiDocumentsQueryForm queryForm) {
        return ResponseDTO.ok(aiDocumentsService.queryPage(queryForm));
    }


    @Operation(summary = "批量删除 @author Mission")
    @PostMapping("/aiDocuments/batchDelete")
    @SaCheckPermission("aiDocuments:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<String> idList) {
        return aiDocumentsService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author Mission")
    @GetMapping("/aiDocuments/delete/{id}")
    @SaCheckPermission("aiDocuments:delete")
    public ResponseDTO<String> batchDelete(@PathVariable String id) {
        return aiDocumentsService.delete(id);
    }
}
