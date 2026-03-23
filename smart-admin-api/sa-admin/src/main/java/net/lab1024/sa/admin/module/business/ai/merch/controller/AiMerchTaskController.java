package net.lab1024.sa.admin.module.business.ai.merch.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.ai.merch.domain.form.AiMerchTaskCreateForm;
import net.lab1024.sa.admin.module.business.ai.merch.domain.form.AiMerchTaskQueryForm;
import net.lab1024.sa.admin.module.business.ai.merch.domain.vo.AiMerchTaskVO;
import net.lab1024.sa.admin.module.business.ai.merch.service.AiMerchTaskService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "AI带货-工作台")
public class AiMerchTaskController {

    @Resource
    private AiMerchTaskService aiMerchTaskService;

    @Operation(summary = "创建任务并生成提示词")
    @PostMapping("/ai/merch/task/create")
    @SaCheckPermission("ai:merch:create")
    public ResponseDTO<AiMerchTaskVO> create(@RequestBody @Valid AiMerchTaskCreateForm createForm) {
        return aiMerchTaskService.createTask(createForm);
    }

    @Operation(summary = "生成图片")
    @PostMapping("/ai/merch/task/generateImage/{taskId}")
    @SaCheckPermission("ai:merch:generateImage")
    public ResponseDTO<AiMerchTaskVO> generateImage(@PathVariable Long taskId) {
        return aiMerchTaskService.generateImage(taskId);
    }

    @Operation(summary = "生成视频")
    @PostMapping("/ai/merch/task/generateVideo/{taskId}")
    @SaCheckPermission("ai:merch:generateVideo")
    public ResponseDTO<AiMerchTaskVO> generateVideo(@PathVariable Long taskId) {
        return aiMerchTaskService.generateVideo(taskId);
    }

    @Operation(summary = "任务详情")
    @GetMapping("/ai/merch/task/detail/{taskId}")
    @SaCheckPermission("ai:merch:query")
    public ResponseDTO<AiMerchTaskVO> detail(@PathVariable Long taskId) {
        return aiMerchTaskService.getDetail(taskId);
    }

    @Operation(summary = "任务分页")
    @PostMapping("/ai/merch/task/queryPage")
    @SaCheckPermission("ai:merch:query")
    public ResponseDTO<PageResult<AiMerchTaskVO>> queryPage(@RequestBody @Valid AiMerchTaskQueryForm queryForm) {
        return ResponseDTO.ok(aiMerchTaskService.queryPage(queryForm));
    }
}
