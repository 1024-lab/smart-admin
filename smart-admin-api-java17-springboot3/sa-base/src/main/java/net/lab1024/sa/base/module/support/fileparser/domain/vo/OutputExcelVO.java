package net.lab1024.sa.base.module.support.fileparser.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class OutputExcelVO {

    @ExcelProperty(value = "购入日期（合同编号）", index = 0)
    private String A;

    @ExcelProperty(value = "喷头型号", index = 1)
    private String B;

    @ExcelProperty(value = "喷头序列号", index = 2)
    private String C="00000-00";

    @ExcelProperty(value = "入仓日期", index = 3)
    private String D;

    @ExcelProperty(value = "领用日期", index = 4)
    private String E;

    @ExcelProperty(value = "领用人", index = 5)
    private String F;

    @ExcelProperty(value = "领用用途", index = 6)
    private String G;

    @ExcelProperty(value = "位置", index = 7)
    private String H;

    @ExcelProperty(value = "历史", index = 8)
    private String I;

}
