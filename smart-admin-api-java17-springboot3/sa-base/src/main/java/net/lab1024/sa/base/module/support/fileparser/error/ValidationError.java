package net.lab1024.sa.base.module.support.fileparser.error;

import lombok.Getter;

@Getter
public class ValidationError {
    private Integer rowNumber;   // Excel 行号（从1开始）
    private Integer columnIndex;    // 列名（如 "购入日期（合同编号）"）
    private String message;       // 错误详情

    public ValidationError(Integer rowNumber, Integer columnIndex, String message) {
        this.rowNumber = rowNumber;
        this.columnIndex = columnIndex;
        this.message = message;
    }

}
