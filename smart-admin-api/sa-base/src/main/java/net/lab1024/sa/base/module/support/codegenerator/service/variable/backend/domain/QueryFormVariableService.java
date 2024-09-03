package net.lab1024.sa.base.module.support.codegenerator.service.variable.backend.domain;

import cn.hutool.core.bean.BeanUtil;
import net.lab1024.sa.base.common.util.SmartEnumUtil;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.module.support.codegenerator.constant.CodeQueryFieldQueryTypeEnum;
import net.lab1024.sa.base.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.model.CodeField;
import net.lab1024.sa.base.module.support.codegenerator.domain.model.CodeQueryField;
import net.lab1024.sa.base.module.support.codegenerator.service.variable.CodeGenerateBaseVariableService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/9/29 17:20:41
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */

public class QueryFormVariableService extends CodeGenerateBaseVariableService {


    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();
        ImmutablePair<List<String>, List<Map<String, Object>>> packageListAndFields = getPackageListAndFields(form);
        variablesMap.put("packageName", form.getBasic().getJavaPackageName() + ".domain.form");
        variablesMap.put("importPackageList", packageListAndFields.getLeft());
        variablesMap.put("fields", packageListAndFields.getRight());
        return variablesMap;
    }


    public ImmutablePair<List<String>, List<Map<String, Object>>> getPackageListAndFields(CodeGeneratorConfigForm form) {

        List<CodeQueryField> fields = form.getQueryFields();

        HashSet<String> packageList = new HashSet<>();

        /**
         * 1、LocalDate、LocalDateTime、BigDecimal 类型的包名
         * 2、排序
         */

        List<Map<String, Object>> finalFieldList = new ArrayList<>();

        for (CodeQueryField field : fields) {

            // CodeField 和 InsertAndUpdateField 合并
            Map<String, Object> finalFieldMap = BeanUtil.beanToMap(field);
            finalFieldMap.putAll(BeanUtil.beanToMap(field));

            String queryTypeEnumStr = field.getQueryTypeEnum();
            CodeQueryFieldQueryTypeEnum queryTypeEnum = SmartEnumUtil.getEnumByValue(queryTypeEnumStr, CodeQueryFieldQueryTypeEnum.class);
            if (queryTypeEnum == null) {
                continue;
            }

            String apiModelProperty = "@Schema(description = \"" + field.getLabel() + "\")";
            finalFieldMap.put("apiModelProperty", apiModelProperty);
            packageList.add("import io.swagger.v3.oas.annotations.media.Schema;");

            CodeField codeField = null;

            switch (queryTypeEnum) {
                case EQUAL:
                    codeField = getCodeFieldByColumnName(field.getColumnNameList().get(0), form);
                    if (codeField == null) {
                        finalFieldMap.put("javaType", "String");
                    } else {
                        finalFieldMap.put("javaType", codeField.getJavaType());
                    }
                    break;
                case DATE_RANGE:
                case DATE:
                    packageList.add("import java.time.LocalDate;");
                    finalFieldMap.put("javaType", "LocalDate");
                    break;
                case ENUM:
                    codeField = getCodeFieldByColumnName(field.getColumnNameList().get(0), form);
                    if (codeField == null) {
                        continue;
                    }

                    packageList.add("import net.lab1024.sa.base.common.swagger.SchemaEnum;");
                    packageList.add("import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;");
                    packageList.add("import " + form.getBasic().getJavaPackageName() + ".constant." + codeField.getEnumName() + ";");

                    //enum check
                    String checkEnum = "@CheckEnum(value = " + codeField.getEnumName() + ".class, message = \"" + codeField.getLabel() + " 错误\")";
                    finalFieldMap.put("apiModelProperty", "@SchemaEnum(value = " + codeField.getEnumName() + ".class, desc = \"" + codeField.getLabel() + "\")");
                    finalFieldMap.put("checkEnum", checkEnum);
                    finalFieldMap.put("isEnum", true);

                    finalFieldMap.put("javaType", codeField.getJavaType());
                    break;
                case DICT:
                    codeField = getCodeFieldByColumnName(field.getColumnNameList().get(0), form);
                    if (SmartStringUtil.isNotEmpty(codeField.getDict())) {
                        finalFieldMap.put("dict", "\n    @JsonDeserialize(using = DictValueVoDeserializer.class)");
                        packageList.add("import com.fasterxml.jackson.databind.annotation.JsonDeserialize;");
                        packageList.add("import net.lab1024.sa.base.common.json.deserializer.DictValueVoDeserializer;");
                    }
                    finalFieldMap.put("javaType", "String");
                default:
                    finalFieldMap.put("javaType", "String");
            }

            finalFieldList.add(finalFieldMap);
        }

        // lombok
        packageList.add("import lombok.Data;");
        packageList.add("import lombok.EqualsAndHashCode;");

        List<String> packageNameList = packageList.stream().filter(Objects::nonNull).sorted().collect(Collectors.toList());
        return ImmutablePair.of(packageNameList, finalFieldList);
    }

}
