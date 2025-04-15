package net.lab1024.sa.base.module.support.fileparser.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OutputExcelVO {

    @ExcelProperty(value = "购入日期（合同编号）", index = 0)
    private String a;

    @ExcelProperty(value = "喷头型号", index = 1)
    private String b;

    @ExcelProperty(value = "喷头序列号", index = 2)
    @NotBlank(message = "C字段不能为空")
    @Pattern(regexp = "^[0-9]{5}-\\d{2}$", message = "C值格式无效，应为00000-000格式")
    private String c;

    @ExcelProperty(value = "入仓日期", index = 3)
    private String d;

    @ExcelProperty(value = "领用日期", index = 4)
    private String e;

    @ExcelProperty(value = "领用人", index = 5)
    private String f;

    @ExcelProperty(value = "领用用途", index = 6)
    private String g;

    @ExcelProperty(value = "位置", index = 7)
    private String h;

    @ExcelProperty(value = "历史", index = 8)
    private String i;

}
