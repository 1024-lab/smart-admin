package net.lab1024.sa.admin.module.business.knowledge.controller;

import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiDocumentSegmentsQueryForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.vo.AiDocumentSegmentsVO;
import net.lab1024.sa.admin.module.business.knowledge.service.AiDocumentSegmentsService;
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
 * 文档分块表 Controller
 *
 * @Author Mission
 * @Date 2026-01-28 20:45:56
 * @Copyright Mission
 */

@RestController
@Tag(name = "文档分块表")
public class AiDocumentSegmentsController {

    @Resource
    private AiDocumentSegmentsService aiDocumentSegmentsService;

    @Operation(summary = "分页查询 @author Mission")
    @PostMapping("/aiDocumentSegments/queryPage")
    @SaCheckPermission("aiDocumentSegments:query")
    public ResponseDTO<PageResult<AiDocumentSegmentsVO>> queryPage(@RequestBody @Valid AiDocumentSegmentsQueryForm queryForm) {
        return ResponseDTO.ok(aiDocumentSegmentsService.queryPage(queryForm));
    }


    @Operation(summary = "批量删除 @author Mission")
    @PostMapping("/aiDocumentSegments/batchDelete")
    @SaCheckPermission("aiDocumentSegments:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return aiDocumentSegmentsService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author Mission")
    @GetMapping("/aiDocumentSegments/delete/{id}")
    @SaCheckPermission("aiDocumentSegments:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return aiDocumentSegmentsService.delete(id);
    }
}
