package net.lab1024.sa.base.module.support.fileparser.validator;

import net.lab1024.sa.base.module.support.fileparser.domain.vo.OutputExcelVO;

import java.util.ArrayList;
import java.util.List;

// 新增校验处理器
public class ExcelValidator {
    public static List<String> validateRow(OutputExcelVO vo, int rowNum) {
        List<String> errors = new ArrayList<>();

        // 必填字段检查
        if (vo.getDynamicFields().get("A2") == null) {
            errors.add("第" + rowNum + "行: 关键字段A2缺失");
        }

        // 数值范围校验
        String power = vo.getDynamicFields().get("power");
        if (power != null && !power.isEmpty()) {
            try {
                int value = Integer.parseInt(power);
                if (value < 100 || value > 1000) {
                    errors.add("第" + rowNum + "行: 功率值超出范围(100-1000)");
                }
            } catch (NumberFormatException e) {
                errors.add("第" + rowNum + "行: 功率值格式错误");
            }
        }

        return errors;
    }
}