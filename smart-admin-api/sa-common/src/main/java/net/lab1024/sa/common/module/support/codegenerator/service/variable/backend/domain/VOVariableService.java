package net.lab1024.sa.common.module.support.codegenerator.service.variable.backend.domain;

import cn.hutool.core.bean.BeanUtil;
import net.lab1024.sa.common.common.util.SmartStringUtil;
import net.lab1024.sa.common.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import net.lab1024.sa.common.module.support.codegenerator.domain.model.CodeField;
import net.lab1024.sa.common.module.support.codegenerator.domain.model.CodeTableField;
import net.lab1024.sa.common.module.support.codegenerator.service.variable.CodeGenerateBaseVariableService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/9/29 17:20:41
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */

public class VOVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();

        Map<String, CodeField> fieldMap = getFieldMap(form);
        List<CodeTableField> updateFieldList = form.getTableFields().stream().filter(e -> Boolean.TRUE.equals(e.getShowFlag())).collect(Collectors.toList());

        ImmutablePair<List<String>, List<Map<String, Object>>> packageListAndFields = getPackageListAndFields(updateFieldList, form);

        variablesMap.put("packageName", form.getBasic().getJavaPackageName() + ".domain.vo");
        variablesMap.put("importPackageList", packageListAndFields.getLeft());
        variablesMap.put("fields", packageListAndFields.getRight());

        return variablesMap;
    }

    public ImmutablePair<List<String>, List<Map<String, Object>>> getPackageListAndFields(List<CodeTableField> fields, CodeGeneratorConfigForm form) {
        if (CollectionUtils.isEmpty(fields)) {
            return ImmutablePair.of(new ArrayList<>(), new ArrayList<>());
        }

        Map<String, CodeField> fieldMap = getFieldMap(form);
        HashSet<String> packageList = new HashSet<>();


        /**
         * 1、LocalDate、LocalDateTime、BigDecimal 类型的包名
         * 2、排序
         */

        List<Map<String, Object>> finalFieldList = new ArrayList<>();

        for (CodeTableField field : fields) {
            CodeField codeField = fieldMap.get(field.getColumnName());
            if (codeField == null) {
                continue;
            }

            // CodeField 和 CodeTableField 合并
            Map<String, Object> finalFieldMap = BeanUtil.beanToMap(field);
            finalFieldMap.putAll(BeanUtil.beanToMap(codeField));

            // 枚举
            if (SmartStringUtil.isNotEmpty(codeField.getEnumName())) {
                packageList.add("import net.lab1024.sa.common.common.swagger.ApiModelPropertyEnum;");
                packageList.add("import " + form.getBasic().getJavaPackageName() + ".constant." + codeField.getEnumName() + ";");

                finalFieldMap.put("apiModelProperty", "@ApiModelPropertyEnum(value = " + codeField.getEnumName() + ".class, desc = \"" + codeField.getLabel() + "\")");
                finalFieldMap.put("isEnum", true);

            } else {
                String apiModelProperty = "@ApiModelProperty(value = \"" + codeField.getLabel() + "\")";
                finalFieldMap.put("apiModelProperty", apiModelProperty);

                packageList.add("import io.swagger.annotations.ApiModelProperty;");
            }


            //字典
            if (isDict(field.getColumnName(), form)) {
                finalFieldMap.put("dict", "\n    @JsonDeserialize(using = DictValueVoDeserializer.class)");
                packageList.add("import com.fasterxml.jackson.databind.annotation.JsonDeserialize;");
                packageList.add("import net.lab1024.sa.common.common.json.deserializer.DictValueVoDeserializer;");
            }

            //文件上传
            if (isFile(field.getColumnName(), form)) {
                finalFieldMap.put("file", "\n    @JsonDeserialize(using = FileKeyVoDeserializer.class)");
                packageList.add("import com.fasterxml.jackson.databind.annotation.JsonDeserialize;");
                packageList.add("import net.lab1024.sa.common.common.json.deserializer.FileKeyVoDeserializer;");
            }

            packageList.add(getJavaPackageName(codeField.getJavaType()));
            finalFieldList.add(finalFieldMap);
        }


        // lombok
        packageList.add("import lombok.Data;");

        List<String> packageNameList = packageList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Collections.sort(packageNameList);
        return ImmutablePair.of(packageNameList, finalFieldList);
    }

}
