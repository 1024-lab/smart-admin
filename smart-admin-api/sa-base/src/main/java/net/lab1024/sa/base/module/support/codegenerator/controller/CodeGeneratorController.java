package net.lab1024.sa.base.module.support.codegenerator.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartResponseUtil;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.form.CodeGeneratorPreviewForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.form.TableQueryForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.vo.TableColumnVO;
import net.lab1024.sa.base.module.support.codegenerator.domain.vo.TableConfigVO;
import net.lab1024.sa.base.module.support.codegenerator.domain.vo.TableVO;
import net.lab1024.sa.base.module.support.codegenerator.service.CodeGeneratorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 代码生成
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-29 20:23:46
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Tag(name = SwaggerTagConst.Support.CODE_GENERATOR)
@Controller
public class CodeGeneratorController extends SupportBaseController {

    @Resource
    private CodeGeneratorService codeGeneratorService;

    // ------------------- 查询 -------------------

    @Operation(summary = "获取表的列 @author 卓大")
    @GetMapping("/codeGenerator/table/getTableColumns/{table}")
    @ResponseBody
    public ResponseDTO<List<TableColumnVO>> getTableColumns(@PathVariable String table) {
        return ResponseDTO.ok(codeGeneratorService.getTableColumns(table));
    }

    @Operation(summary = "查询数据库的表 @author 卓大")
    @PostMapping("/codeGenerator/table/queryTableList")
    @ResponseBody
    public ResponseDTO<PageResult<TableVO>> queryTableList(@RequestBody @Valid TableQueryForm tableQueryForm) {
        return ResponseDTO.ok(codeGeneratorService.queryTableList(tableQueryForm));
    }

    // ------------------- 配置 -------------------

    @Operation(summary = "获取表的配置信息 @author 卓大")
    @GetMapping("/codeGenerator/table/getConfig/{table}")
    @ResponseBody
    public ResponseDTO<TableConfigVO> getTableConfig(@PathVariable String table) {
        return ResponseDTO.ok(codeGeneratorService.getTableConfig(table));
    }

    @Operation(summary = "更新配置信息 @author 卓大")
    @PostMapping("/codeGenerator/table/updateConfig")
    @ResponseBody
    public ResponseDTO<String> updateConfig(@RequestBody @Valid CodeGeneratorConfigForm form) {
        return codeGeneratorService.updateConfig(form);
    }

    // ------------------- 生成 -------------------

    @Operation(summary = "代码预览 @author 卓大")
    @PostMapping("/codeGenerator/code/preview")
    @ResponseBody
    public ResponseDTO<String> preview(@RequestBody @Valid CodeGeneratorPreviewForm form) {
        return codeGeneratorService.preview(form);
    }

    @Operation(summary = "代码下载 @author 卓大")
    @GetMapping(value = "/codeGenerator/code/download/{tableName}", produces = "application/octet-stream")
    public void download(@PathVariable String tableName, HttpServletResponse response) throws IOException {

        ResponseDTO<byte[]> download = codeGeneratorService.download(tableName);

        if (download.getOk()) {
            SmartResponseUtil.setDownloadFileHeader(response, tableName + "_code.zip", (long) download.getData().length);
            response.getOutputStream().write(download.getData());
        } else {
            SmartResponseUtil.write(response, download);
        }
    }

}