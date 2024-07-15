package net.lab1024.sa.admin.module.system.position.controller;

import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.system.position.domain.form.PositionAddForm;
import net.lab1024.sa.admin.module.system.position.domain.form.PositionQueryForm;
import net.lab1024.sa.admin.module.system.position.domain.form.PositionUpdateForm;
import net.lab1024.sa.admin.module.system.position.domain.vo.PositionVO;
import net.lab1024.sa.admin.module.system.position.service.PositionService;
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

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 职务表 Controller
 *
 * @Author kaiyun
 * @Date 2024-06-23 23:31:38
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */

@RestController
@Tag(name = AdminSwaggerTagConst.System.SYSTEM_POSITION)
public class PositionController {

    @Resource
    private PositionService positionService;

    @Operation(summary = "分页查询 @author kaiyun")
    @PostMapping("/position/queryPage")
    public ResponseDTO<PageResult<PositionVO>> queryPage(@RequestBody @Valid PositionQueryForm queryForm) {
        return ResponseDTO.ok(positionService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author kaiyun")
    @PostMapping("/position/add")
    public ResponseDTO<String> add(@RequestBody @Valid PositionAddForm addForm) {
        return positionService.add(addForm);
    }

    @Operation(summary = "更新 @author kaiyun")
    @PostMapping("/position/update")
    public ResponseDTO<String> update(@RequestBody @Valid PositionUpdateForm updateForm) {
        return positionService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author kaiyun")
    @PostMapping("/position/batchDelete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return positionService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author kaiyun")
    @GetMapping("/position/delete/{positionId}")
    public ResponseDTO<String> batchDelete(@PathVariable Long positionId) {
        return positionService.delete(positionId);
    }


    @Operation(summary = "不分页查询 @author kaiyun")
    @GetMapping("/position/queryList")
    public ResponseDTO<List<PositionVO>> queryList() {
        return ResponseDTO.ok(positionService.queryList());
    }
}
