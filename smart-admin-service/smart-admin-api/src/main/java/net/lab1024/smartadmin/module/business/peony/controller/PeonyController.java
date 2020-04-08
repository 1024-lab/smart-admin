package net.lab1024.smartadmin.module.business.peony.controller;

import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.controller.BaseController;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.common.domain.ValidateList;
import net.lab1024.smartadmin.module.business.peony.domain.dto.PeonyAddDTO;
import net.lab1024.smartadmin.module.business.peony.domain.dto.PeonyUpdateDTO;
import net.lab1024.smartadmin.module.business.peony.domain.dto.PeonyQueryDTO;
import net.lab1024.smartadmin.module.business.peony.domain.vo.PeonyVO;
import net.lab1024.smartadmin.module.business.peony.domain.vo.PeonyExcelVO;
import net.lab1024.smartadmin.module.business.peony.service.PeonyService;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import org.apache.poi.ss.usermodel.Workbook;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * [ 牡丹花 ]
 *
 * @author 卓大
 * @version 1.0
 * @company 1024创新实验室( www.1024lab.net )
 * @copyright (c)  1024创新实验室( www.1024lab.net )Inc. All rights reserved.
 * @date 2020-04-06 18:17:56
 * @since JDK1.8
 */
@RestController
@Api(tags = {"牡丹花"})
public class PeonyController extends BaseController {

    @Autowired
    private PeonyService peonyService;

    @ApiOperation(value = "分页查询牡丹花",notes = "@author 卓大")
    @PostMapping("/peony/page/query")
    public ResponseDTO<PageResultDTO<PeonyVO>> queryByPage(@RequestBody PeonyQueryDTO queryDTO) {
        return peonyService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "添加牡丹花",notes = "@author 卓大")
    @PostMapping("/peony/add")
    public ResponseDTO<String> add(@RequestBody @Validated PeonyAddDTO addTO){
        return peonyService.add(addTO);
    }

    @ApiOperation(value="修改牡丹花",notes = "@author 卓大")
    @PostMapping("/peony/update")
    public ResponseDTO<String> update(@RequestBody @Validated PeonyUpdateDTO updateDTO){
        return peonyService.update(updateDTO);
    }

    @ApiOperation(value="批量删除牡丹花",notes = "@author 卓大")
    @PostMapping("/peony/deleteByIds")
    public ResponseDTO<String> delete(@RequestBody @Validated ValidateList<Long> idList) {
        return peonyService.deleteByIds(idList);
    }

    @ApiOperation(value = "批量导出", notes = "@author 卓大")
    @PostMapping("/peony/export/batch")
    public void batchExport(@RequestBody @Validated ValidateList<Long> idList, HttpServletResponse response) {
        //查询数据
        List<PeonyExcelVO> peonyList = peonyService.queryBatchExportData(idList);
        //导出操作
        ExportParams ex = new ExportParams("牡丹花", "Sheet1");
        Workbook workbook = ExcelExportUtil.exportExcel(ex, PeonyExcelVO.class, peonyList);
        downloadExcel("牡丹花", workbook, response);
    }

    @ApiOperation(value = "导出全部", notes = "@author 卓大")
    @PostMapping("/peony/export/all")
    public void exportAll(@RequestBody @Validated PeonyQueryDTO queryDTO, HttpServletResponse response) {
        //查询数据
        List<PeonyExcelVO> peonyList = peonyService.queryAllExportData(queryDTO);
        //导出操作
        ExportParams ex = new ExportParams("牡丹花", "Sheet1");
        Workbook workbook = ExcelExportUtil.exportExcel(ex, PeonyExcelVO.class, peonyList);
        downloadExcel("牡丹花", workbook, response);
    }

}
