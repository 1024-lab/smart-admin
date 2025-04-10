package net.lab1024.sa.base.module.support.fileparser.controller;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.ExcelWriter;
import cn.idev.excel.write.metadata.WriteSheet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.LocalDateParseUtil;
import net.lab1024.sa.base.common.util.SmartDateFormatterEnum;
import net.lab1024.sa.base.common.util.SmartExcelUtil;
import net.lab1024.sa.base.common.util.SmartLocalDateUtil;
import net.lab1024.sa.base.common.util.SmartResponseUtil;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.datatracer.domain.form.DataTracerQueryForm;
import net.lab1024.sa.base.module.support.datatracer.domain.vo.DataTracerVO;
import net.lab1024.sa.base.module.support.fileparser.domain.vo.OutputExcelVO;
import net.lab1024.sa.base.module.support.fileparser.generator.HeaderGenerator;
import net.lab1024.sa.base.module.support.fileparser.wrapper.DataWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        String fileName = "file1.xlsx";
        try(InputStream stream = importExcel.getInputStream()){
            // 使用POI解析为导入DTO
            Workbook workbook = new XSSFWorkbook(stream);
            List<OutputExcelVO> data = new ArrayList<>();
            List<List<String>> headers = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(i);
                for (int j =0; j <= sheet.getLastRowNum(); j++) {
                    Row row = sheet.getRow(j);
                    if(row == null) continue;
                    if(row.getRowNum() == 0) {
                        for(int k=0;k<=row.getLastCellNum();k++){
                            String header = safeGetCellValue(row, k);
                            if(header!=""){
                                headers.add(Collections.singletonList(header));
                            }
                        }
                    }else{
                        OutputExcelVO outputExcelVO = new OutputExcelVO();
                        for(int k = 0; k <= row.getLastCellNum(); k++) {
                            Cell cell = row.getCell(k);
                            outputExcelVO.getDynamicFields().put("A" + k, safeGetCellValue(row, k));
                        }
                        data.add(outputExcelVO);
                    }
                }
            }
//            // 1. 准备表头和数据
//            List<List<String>> headers = HeaderGenerator.generateDynamicHeaders();
            List<List<Object>> dataRows = DataWrapper.wrapData(data);
            // 2. 创建写入器
            ExcelWriter excelWriter = EasyExcel.write(fileName)
                    .head(headers)
                    .build();

            // 3. 写入数据
            WriteSheet sheet = EasyExcel.writerSheet(fileName).build();
            excelWriter.write(dataRows, sheet);

            // 4. 关闭资源
            excelWriter.finish();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String safeGetCellValue(Row row, int columnIndex) {
        if (row == null) {
            return "";
        }
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return "";
        }

        // 根据单元格类型处理
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
