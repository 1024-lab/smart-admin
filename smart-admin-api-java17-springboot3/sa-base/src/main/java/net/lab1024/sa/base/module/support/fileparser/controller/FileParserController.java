package net.lab1024.sa.base.module.support.fileparser.controller;

import cn.idev.excel.EasyExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.fileparser.domain.vo.OutputExcelVO;
import net.lab1024.sa.base.module.support.fileparser.processor.ExcelProcessor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/**
 * 文件解析
 *
 * @Author 海印:芦苇
 */
@Tag(name = SwaggerTagConst.Support.FILE_PARSER)
@RestController
public class FileParserController extends SupportBaseController {

//    @Operation(summary = "导出企业信息 @author 卓大")
//    @PostMapping("/oa/enterprise/exportExcel")
//    public void exportExcel(@RequestBody @Valid EnterpriseQueryForm queryForm, HttpServletResponse response) throws IOException {
//        List<EnterpriseExcelVO> data = enterpriseService.getExcelExportData(queryForm);
//        if (CollectionUtils.isEmpty(data)) {
//            SmartResponseUtil.write(response, ResponseDTO.userErrorParam("暂无数据"));
//            return;
//        }
//
//        String watermark = AdminRequestUtil.getRequestUser().getActualName();
//        watermark += SmartLocalDateUtil.format(LocalDateTime.now(), SmartDateFormatterEnum.YMD_HMS);
//
//        SmartExcelUtil.exportExcelWithWatermark(response,"企业基本信息.xlsx","企业信息",EnterpriseExcelVO.class,data,watermark);
//
//    }

    /**
     * 生成动态表头（示例数据）
     * @return 表头列表，格式为 List<列头层级>
     */
    @Operation(summary = "数据排序导出 - @author 芦苇")
    @PostMapping("/fileParser/exportExcel")
    public void exportExcel(@RequestPart("file") MultipartFile importExcel, HttpServletResponse response) {
        ExcelProcessor processor = new ExcelProcessor();
        try {
            processor.process(importExcel.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 获取处理结果
        List<OutputExcelVO> finalData = processor.getMergedData();
        List<String> errorLogs = processor.getErrors();

        // 输出错误日志
        if (!errorLogs.isEmpty()) {
            System.err.println("发现错误: \n" +
                    String.join("\n", errorLogs));
        }

        // 导出处理后的数据
        EasyExcel.write("output.xlsx", OutputExcelVO.class)
                .sheet("合并数据")
                .doWrite(finalData);
    }

    private String safeGetCellValue(Row row, int column) {
        if (row == null) return "";

        Cell cell = row.getCell(column, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";

        try {
            return switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue().trim();
                case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
                case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                default -> "";
            };
        } catch (Exception e) {
            return "[解析错误]"; // 标记异常单元格
        }
    }
}
