package net.lab1024.sa.admin.module.business.ai.controller;

import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppAddForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppQueryForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppUpdateForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiAppVO;
import net.lab1024.sa.admin.module.business.ai.service.AiAppService;
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
 * ai应用管理表 Controller
 *
 * @Author Mission
 * @Date 2025-12-21 16:00:44
 * @Copyright Mission
 */

@RestController
@Tag(name = "ai应用管理表")
public class AiAppController {

    @Resource
    private AiAppService aiAppService;

    @Operation(summary = "分页查询 @author Mission")
    @PostMapping("/aiApp/queryPage")
    @SaCheckPermission("aiApp:query")
    public ResponseDTO<PageResult<AiAppVO>> queryPage(@RequestBody @Valid AiAppQueryForm queryForm) {
        return ResponseDTO.ok(aiAppService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author Mission")
    @PostMapping("/aiApp/add")
    @SaCheckPermission("aiApp:add")
    public ResponseDTO<String> add(@RequestBody @Valid AiAppAddForm addForm) {
        return aiAppService.add(addForm);
    }

    @Operation(summary = "更新 @author Mission")
    @PostMapping("/aiApp/update")
    @SaCheckPermission("aiApp:update")
    public ResponseDTO<String> update(@RequestBody @Valid AiAppUpdateForm updateForm) {
        return aiAppService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author Mission")
    @PostMapping("/aiApp/batchDelete")
    @SaCheckPermission("aiApp:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<String> idList) {
        return aiAppService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author Mission")
    @GetMapping("/aiApp/delete/{id}")
    @SaCheckPermission("aiApp:delete")
    public ResponseDTO<String> batchDelete(@PathVariable String id) {
        return aiAppService.delete(id);
    }
}
