package net.lab1024.sa.admin.module.business.ai.controller;

//import net.lab1024.sa.admin.module.business.ai.domain.form.AiConversationsAddForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiConversationsQueryForm;
//import net.lab1024.sa.admin.module.business.ai.domain.form.AiConversationsUpdateForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiConversationsVO;
import net.lab1024.sa.admin.module.business.ai.service.AiConversationsService;
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
 * 会话表 Controller
 *
 * @Author Mission
 * @Date 2025-12-21 16:08:28
 * @Copyright Mission
 */

@RestController
@Tag(name = "会话表")
public class AiConversationsController {

    @Resource
    private AiConversationsService aiConversationsService;

    @Operation(summary = "分页查询 @author Mission")
    @PostMapping("/aiConversations/queryPage")
    @SaCheckPermission("aiConversations:query")
    public ResponseDTO<PageResult<AiConversationsVO>> queryPage(@RequestBody @Valid AiConversationsQueryForm queryForm) {
        return ResponseDTO.ok(aiConversationsService.queryPage(queryForm));
    }


    @Operation(summary = "批量删除 @author Mission")
    @PostMapping("/aiConversations/batchDelete")
    @SaCheckPermission("aiConversations:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<String> idList) {
        return aiConversationsService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author Mission")
    @GetMapping("/aiConversations/delete/{id}")
    @SaCheckPermission("aiConversations:delete")
    public ResponseDTO<String> batchDelete(@PathVariable String id) {
        return aiConversationsService.delete(id);
    }
}
