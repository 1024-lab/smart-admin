package net.lab1024.sa.base.module.support.fileparser.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class OutputExcelVO {

    @ExcelProperty("购入日期（合同编号）")
    private String A;

    @ExcelProperty("喷头型号")
    private String B;

    @ExcelProperty("喷头序列号")
    private String C;

    @ExcelProperty("入仓日期")
    private String D;

    @ExcelProperty("领用日期")
    private String E;

    @ExcelProperty("领用人")
    private String F;

    @ExcelProperty("领用用途")
    private String G;

    @ExcelProperty("位置")
    private String H;

    @ExcelProperty("历史")
    private String I;

}
