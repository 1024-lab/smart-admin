package net.lab1024.sa.admin.module.business.ai.controller;

//import net.lab1024.sa.admin.module.business.ai.domain.form.AiMessagesAddForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiMessagesQueryForm;
//import net.lab1024.sa.admin.module.business.ai.domain.form.AiMessagesUpdateForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiMessagesVO;
import net.lab1024.sa.admin.module.business.ai.service.AiMessagesService;
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
 * 消息表 Controller
 *
 * @Author Mission
 * @Date 2025-12-21 16:10:16
 * @Copyright Mission
 */

@RestController
@Tag(name = "消息表")
public class AiMessagesController {

    @Resource
    private AiMessagesService aiMessagesService;

    @Operation(summary = "分页查询 @author Mission")
    @PostMapping("/aiMessages/queryPage")
    @SaCheckPermission("aiMessages:query")
    public ResponseDTO<PageResult<AiMessagesVO>> queryPage(@RequestBody @Valid AiMessagesQueryForm queryForm) {
        return ResponseDTO.ok(aiMessagesService.queryPage(queryForm));
    }


    @Operation(summary = "批量删除 @author Mission")
    @PostMapping("/aiMessages/batchDelete")
    @SaCheckPermission("aiMessages:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<String> idList) {
        return aiMessagesService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author Mission")
    @GetMapping("/aiMessages/delete/{id}")
    @SaCheckPermission("aiMessages:delete")
    public ResponseDTO<String> batchDelete(@PathVariable String id) {
        return aiMessagesService.delete(id);
    }
}
