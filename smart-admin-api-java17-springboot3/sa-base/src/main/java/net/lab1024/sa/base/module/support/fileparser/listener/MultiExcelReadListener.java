package net.lab1024.sa.base.module.support.fileparser.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import cn.idev.excel.exception.ExcelDataConvertException;
import lombok.Data;
import net.lab1024.sa.base.module.support.fileparser.error.ValidationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MultiExcelReadListener<T> extends AnalysisEventListener<T> {
    List<Map<String, Object>> dataList = new ArrayList<>();
    Map<String, Object> rowData = new HashMap<>();
    private final List<ValidationError> errors = new ArrayList<>();

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        super.onException(exception, context);
        //捕获校验异常，记录错误详情
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException ex = (ExcelDataConvertException) exception;
            errors.add(new ValidationError(
                    ex.getRowIndex(),
                    ex.getColumnIndex(),
                    "字段类型不匹配" + ex.getCellData()
            ));
        }
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        rowData.put("__fileName", context.readSheetHolder().getSheetName());// 记录来源文件
        rowData.put("__rowNum", context.readRowHolder().getRowIndex()); // 记录行号
        rowData.put("data", data);
        dataList.add(rowData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
