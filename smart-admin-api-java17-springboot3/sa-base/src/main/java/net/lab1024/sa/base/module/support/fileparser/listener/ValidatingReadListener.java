package net.lab1024.sa.base.module.support.fileparser.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import cn.idev.excel.exception.ExcelDataConvertException;
import jakarta.validation.Valid;
import net.lab1024.sa.base.module.support.fileparser.domain.vo.OutputExcelVO;
import net.lab1024.sa.base.module.support.fileparser.error.ValidationError;

import java.util.ArrayList;
import java.util.List;


public class ValidatingReadListener extends AnalysisEventListener<OutputExcelVO> {
    private final List<OutputExcelVO> validData = new ArrayList<>();
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
    public void invoke(@Valid OutputExcelVO data, AnalysisContext context) {
        //自动触发校验，无效数据直接跳过
        validData.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public List<OutputExcelVO> getValidData() {
        return validData;
    }
}
