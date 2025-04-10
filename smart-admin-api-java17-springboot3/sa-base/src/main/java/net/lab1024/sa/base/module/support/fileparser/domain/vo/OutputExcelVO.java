package net.lab1024.sa.base.module.support.fileparser.domain.vo;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class OutputExcelVO {
    @Pattern(regexp = "^\\d{5}-\\d{2}$", message = "编码格式必须为5位数字-2位数字")
    private String mainCode;

    // 动态扩展字段（Key为字段标识，Value为值）
    private Map<String, String> dynamicFields = new LinkedHashMap<>();
}
