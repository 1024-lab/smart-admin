package net.lab1024.sa.admin.module.business.ai.controller;

import net.lab1024.sa.admin.module.business.ai.domain.form.AiModelConfigAddForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiModelConfigQueryForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiModelConfigUpdateForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiModelConfigVO;
import net.lab1024.sa.admin.module.business.ai.service.AiModelConfigService;
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
 * 模型配置表 Controller
 *
 * @Author Mission
 * @Date 2026-01-28 09:59:38
 * @Copyright Mission
 */

@RestController
@Tag(name = "模型配置表")
public class AiModelConfigController {

    @Resource
    private AiModelConfigService aiModelConfigService;

    @Operation(summary = "分页查询 @author Mission")
    @PostMapping("/aiModelConfig/queryPage")
    @SaCheckPermission("aiModelConfig:query")
    public ResponseDTO<PageResult<AiModelConfigVO>> queryPage(@RequestBody @Valid AiModelConfigQueryForm queryForm) {
        return ResponseDTO.ok(aiModelConfigService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author Mission")
    @PostMapping("/aiModelConfig/add")
    @SaCheckPermission("aiModelConfig:add")
    public ResponseDTO<String> add(@RequestBody @Valid AiModelConfigAddForm addForm) {
        return aiModelConfigService.add(addForm);
    }

    @Operation(summary = "更新 @author Mission")
    @PostMapping("/aiModelConfig/update")
    @SaCheckPermission("aiModelConfig:update")
    public ResponseDTO<String> update(@RequestBody @Valid AiModelConfigUpdateForm updateForm) {
        return aiModelConfigService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author Mission")
    @PostMapping("/aiModelConfig/batchDelete")
    @SaCheckPermission("aiModelConfig:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Integer> idList) {
        return aiModelConfigService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author Mission")
    @GetMapping("/aiModelConfig/delete/{id}")
    @SaCheckPermission("aiModelConfig:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Integer id) {
        return aiModelConfigService.delete(id);
    }

    @Operation(summary = "根据模型类型查询模型列表 @author Mission")
    @GetMapping("/aiModelConfig/queryListByModelType/{modelType}")
    public ResponseDTO<List<AiModelConfigVO>> queryListByModelType(@PathVariable String modelType) {
        return ResponseDTO.ok(aiModelConfigService.queryListByModelType(modelType));
    }
}
