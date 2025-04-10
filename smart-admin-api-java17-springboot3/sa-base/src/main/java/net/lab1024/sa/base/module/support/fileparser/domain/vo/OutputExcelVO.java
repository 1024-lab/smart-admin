package net.lab1024.sa.base.module.support.fileparser.domain.vo;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class OutputExcelVO {
    // 固定基础字段
    private String baseField1;

    // 动态扩展字段（Key为字段标识，Value为值）
    private Map<String, Object> dynamicFields = new LinkedHashMap<>();
}
