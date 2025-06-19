package net.lab1024.sa.base.module.support.codegenerator.service.variable.front;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.base.CaseFormat;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.module.support.codegenerator.constant.CodeFrontComponentEnum;
import net.lab1024.sa.base.module.support.codegenerator.constant.CodeQueryFieldQueryTypeEnum;
import net.lab1024.sa.base.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.model.CodeField;
import net.lab1024.sa.base.module.support.codegenerator.domain.model.CodeInsertAndUpdateField;
import net.lab1024.sa.base.module.support.codegenerator.domain.model.CodeQueryField;
import net.lab1024.sa.base.module.support.codegenerator.domain.model.CodeTableField;
import net.lab1024.sa.base.module.support.codegenerator.service.variable.CodeGenerateBaseVariableService;

import java.util.*;

/**
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/9/29 17:20:41
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */

public class ListVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();


        HashSet<String> frontImportSet = new HashSet<>();
        frontImportSet.add("import " + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, form.getBasic().getModuleName()) + "Form from './" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, form.getBasic().getModuleName()) + "-form.vue';");

        // 查询参数
        List<Map<String, Object>> queryVariable = new ArrayList<>();
        List<CodeQueryField> queryFields = form.getQueryFields();

        for (CodeQueryField queryField : queryFields) {
            Map<String, Object> objectMap = BeanUtil.beanToMap(queryField);

            CodeField codeField = getCodeFieldByColumnName(queryField.getColumnNameList().get(0), form);

            if (CodeQueryFieldQueryTypeEnum.ENUM.equalsValue(queryField.getQueryTypeEnum()) && SmartStringUtil.isNotBlank(codeField.getEnumName())) {
                String upperUnderscoreEnum = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, codeField.getEnumName());
                objectMap.put("frontEnumName", upperUnderscoreEnum);
                frontImportSet.add("import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';");
            }

            if (CodeQueryFieldQueryTypeEnum.DICT.equalsValue(queryField.getQueryTypeEnum())) {
                objectMap.put("dict", codeField.getDict());
                frontImportSet.add("import DictSelect from '/@/components/support/dict-select/index.vue';");
            }

            if (CodeQueryFieldQueryTypeEnum.DATE_RANGE.equalsValue(queryField.getQueryTypeEnum())) {
                frontImportSet.add("import { defaultTimeRanges } from '/@/lib/default-time-ranges';");
            }
            queryVariable.add(objectMap);
        }

        // 表格列表
        List<Map<String, Object>> listVariable = new ArrayList<>();
        // 过滤掉不显示的字段
        List<CodeTableField> tableFields = form.getTableFields().stream().filter(CodeTableField::getShowFlag).toList();

        for (CodeTableField tableField : tableFields) {
            Map<String, Object> objectMap = BeanUtil.beanToMap(tableField);
            objectMap.put("fieldName", tableField.getFieldName());

            CodeField codeField = getCodeFieldByColumnName(tableField.getColumnName(), form);
            if (codeField == null) {
                continue;
            }

            // 是否存在枚举
            if (SmartStringUtil.isNotBlank(codeField.getEnumName())) {
                String upperUnderscoreEnum = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, codeField.getEnumName());
                objectMap.put("frontEnumPlugin", "$smartEnumPlugin.getDescByValue('" + upperUnderscoreEnum + "', text)");
            }

            // 是否存在字典
            if (SmartStringUtil.isNotBlank(codeField.getDict())) {
                objectMap.put("dict", codeField.getDict());
                frontImportSet.add("import { DICT_CODE_ENUM } from '/@/constants/support/dict-const.js';");
                frontImportSet.add("import DictLabel from '/@/components/support/dict-label/index.vue';");
            }

            CodeInsertAndUpdateField codeInsertAndUpdateField = form.getInsertAndUpdate().getFieldList().stream().filter(e -> SmartStringUtil.equals(tableField.getColumnName(), e.getColumnName())).findFirst().orElse(null);
            if (codeInsertAndUpdateField == null) {
                continue;
            }

            // 是否存在上传组件
            if (CodeFrontComponentEnum.FILE_UPLOAD.equalsValue(codeInsertAndUpdateField.getFrontComponent())) {
                objectMap.put("frontComponent", codeInsertAndUpdateField.getFrontComponent());
                frontImportSet.add("import FilePreview from '/@/components/support/file-preview/index.vue';");
            }

            listVariable.add(objectMap);
        }

        variablesMap.put("queryFields", queryVariable);
        variablesMap.put("listFields", listVariable);
        variablesMap.put("frontImportList", new ArrayList<>(frontImportSet));

        return variablesMap;
    }
}
