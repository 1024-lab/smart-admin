package net.lab1024.sa.common.module.support.codegenerator.service.variable.backend.domain;

import cn.hutool.core.bean.BeanUtil;
import net.lab1024.sa.common.module.support.codegenerator.constant.CodeQueryFieldQueryTypeEnum;
import net.lab1024.sa.common.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import net.lab1024.sa.common.module.support.codegenerator.domain.model.CodeInsertAndUpdateField;
import net.lab1024.sa.common.module.support.codegenerator.domain.model.CodeQueryField;
import net.lab1024.sa.common.module.support.codegenerator.service.variable.CodeGenerateBaseVariableService;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/9/29 17:20:41
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
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
                    stringBuilder.append("        AND INSTR(" )//
                            .append(form.getTableName()).append("." ).append(queryField.getColumnNameList().get(0))
                            .append(",#{queryForm." )
                            .append(queryField.getFieldName())
                            .append("})" );
                } else {
                    for (int i = 0; i < columnNameList.size(); i++) {
                        if (i == 0) {
                            stringBuilder.append("AND ( INSTR(" )//
                                    .append(form.getTableName()).append("." ).append(queryField.getColumnNameList().get(i))
                                    .append(",#{queryForm." )
                                    .append(queryField.getFieldName())
                                    .append("})" );
                        } else {
                            // OR INSTR(t_notice.author,#{query.keywords})
                            stringBuilder.append("\n                OR INSTR(" )//
                                    .append(form.getTableName()).append("." ).append(queryField.getColumnNameList().get(i))
                                    .append(",#{queryForm." )
                                    .append(queryField.getFieldName())
                                    .append("})" );
                        }
                    }
                    stringBuilder.append("\n            )" );
                }
                fieldMap.put("likeStr", stringBuilder.toString());
            }else{
                fieldMap.put("columnName",queryField.getColumnNameList().get(0));
            }
        }

        variablesMap.put("queryFields", finalQueryFiledList);
        variablesMap.put("daoClassName", form.getBasic().getJavaPackageName() + ".dao." + form.getBasic().getModuleName() + "Dao" );
        return variablesMap;
    }


    public List<String> getPackageList(List<CodeInsertAndUpdateField> fields, CodeGeneratorConfigForm form) {
        if (CollectionUtils.isEmpty(fields)) {
            return new ArrayList<>();
        }

        HashSet<String> packageList = new HashSet<>();

        //1、javabean相关的包
        packageList.addAll(getJavaBeanImportClass(form));

        //2、dao
        packageList.add("import " + form.getBasic().getJavaPackageName() + ".dao." + form.getBasic().getModuleName() + "Dao;" );
        return new ArrayList<>(packageList);
    }

}
