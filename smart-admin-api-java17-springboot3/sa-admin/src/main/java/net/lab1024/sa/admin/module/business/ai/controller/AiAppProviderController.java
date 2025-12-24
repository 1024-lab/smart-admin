package net.lab1024.sa.admin.module.business.ai.controller;

import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppProviderAddForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppProviderQueryForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppProviderUpdateForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiAppProviderVO;
import net.lab1024.sa.admin.module.business.ai.service.AiAppProviderService;
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

import java.util.List;

/**
 * 应用提供商 Controller
 *
 * @Author Mission
 * @Date 2025-12-21 16:06:19
 * @Copyright Mission
 */

@RestController
@Tag(name = "应用提供商")
public class AiAppProviderController {

    @Resource
    private AiAppProviderService aiAppProviderService;

    @Operation(summary = "分页查询 @author Mission")
    @PostMapping("/aiAppProvider/queryPage")
    @SaCheckPermission("aiAppProvider:query")
    public ResponseDTO<PageResult<AiAppProviderVO>> queryPage(@RequestBody @Valid AiAppProviderQueryForm queryForm) {
        return ResponseDTO.ok(aiAppProviderService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author Mission")
    @PostMapping("/aiAppProvider/add")
    @SaCheckPermission("aiAppProvider:add")
    public ResponseDTO<String> add(@RequestBody @Valid AiAppProviderAddForm addForm) {
        return aiAppProviderService.add(addForm);
    }

    @Operation(summary = "更新 @author Mission")
    @PostMapping("/aiAppProvider/update")
    @SaCheckPermission("aiAppProvider:update")
    public ResponseDTO<String> update(@RequestBody @Valid AiAppProviderUpdateForm updateForm) {
        return aiAppProviderService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author Mission")
    @PostMapping("/aiAppProvider/batchDelete")
    @SaCheckPermission("aiAppProvider:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<String> idList) {
        return aiAppProviderService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author Mission")
    @GetMapping("/aiAppProvider/delete/{id}")
    @SaCheckPermission("aiAppProvider:delete")
    public ResponseDTO<String> batchDelete(@PathVariable String id) {
        return aiAppProviderService.delete(id);
    }

    @Operation(summary = "查询所有供应商 @author Mission")
    @GetMapping("/aiAppProvider/queryAll")
    @SaCheckPermission("aiAppProvider:query")
    public ResponseDTO<List<AiAppProviderVO>> queryAll() {
        return ResponseDTO.ok(aiAppProviderService.queryAll());
    }
}
