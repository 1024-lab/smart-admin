package net.lab1024.sa.base.module.support.fileparser.controller;

import cn.idev.excel.EasyExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.fileparser.domain.vo.OutputExcelVO;
import net.lab1024.sa.base.module.support.fileparser.processor.ExcelProcessor;
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


    /**
     * 生成动态表头（示例数据）
     * @return 表头列表，格式为 List<列头层级>
     */
    @Operation(summary = "数据排序导出 - @author 芦苇")
    @PostMapping("/fileParser/exportExcel")
    public void exportExcel(@RequestPart("file") MultipartFile importExcel, HttpServletResponse response) throws IOException {
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

}
