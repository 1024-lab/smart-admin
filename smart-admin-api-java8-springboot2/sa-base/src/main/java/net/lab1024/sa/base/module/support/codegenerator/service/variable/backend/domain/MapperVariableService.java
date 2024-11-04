package net.lab1024.sa.base.module.support.codegenerator.service.variable.backend.domain;

import cn.hutool.core.bean.BeanUtil;
import net.lab1024.sa.base.module.support.codegenerator.constant.CodeQueryFieldQueryTypeEnum;
import net.lab1024.sa.base.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.model.CodeQueryField;
import net.lab1024.sa.base.module.support.codegenerator.service.variable.CodeGenerateBaseVariableService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/9/29 17:20:41
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */

public class MapperVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();
        List<Map<String, Object>> finalQueryFiledList = new ArrayList<>();
        for (CodeQueryField queryField : form.getQueryFields()) {
            Map<String, Object> fieldMap = BeanUtil.beanToMap(queryField);
            finalQueryFiledList.add(fieldMap);

            //模糊查询
            if (CodeQueryFieldQueryTypeEnum.LIKE.getValue().equals(queryField.getQueryTypeEnum())) {
                StringBuilder stringBuilder = new StringBuilder();
                List<String> columnNameList = queryField.getColumnNameList();
                if (columnNameList.size() == 1) {
                    // AND INSTR(t_notice.title,#{query.keywords})
                    stringBuilder.append("        AND INSTR(")
                            .append(form.getTableName()).append(".").append(queryField.getColumnNameList().get(0))
                            .append(",#{queryForm.")
                            .append(queryField.getFieldName())
                            .append("})");
                } else {
                    for (int i = 0; i < columnNameList.size(); i++) {
                        if (i == 0) {
                            stringBuilder.append("AND ( INSTR(")
                                    .append(form.getTableName()).append(".").append(queryField.getColumnNameList().get(i))
                                    .append(",#{queryForm.")
                                    .append(queryField.getFieldName())
                                    .append("})");
                        } else {
                            // OR INSTR(t_notice.author,#{query.keywords})
                            stringBuilder.append("\n                OR INSTR(")
                                    .append(form.getTableName()).append(".").append(queryField.getColumnNameList().get(i))
                                    .append(",#{queryForm.")
                                    .append(queryField.getFieldName())
                                    .append("})");
                        }
                    }
                    stringBuilder.append("\n            )");
                }
                fieldMap.put("likeStr", stringBuilder.toString());
            } else if (CodeQueryFieldQueryTypeEnum.DICT.equalsValue(queryField.getQueryTypeEnum())) {
                String stringBuilder = "AND INSTR(" +
                        form.getTableName() + "." + queryField.getColumnNameList().get(0) +
                        ",#{queryForm." +
                        queryField.getFieldName() +
                        "})";
                fieldMap.put("likeStr", stringBuilder);
            }
            else {
                fieldMap.put("columnName", queryField.getColumnNameList().get(0));
            }
        }

        variablesMap.put("queryFields", finalQueryFiledList);
        variablesMap.put("daoClassName", form.getBasic().getJavaPackageName() + ".dao." + form.getBasic().getModuleName() + "Dao");
        return variablesMap;
    }

}
