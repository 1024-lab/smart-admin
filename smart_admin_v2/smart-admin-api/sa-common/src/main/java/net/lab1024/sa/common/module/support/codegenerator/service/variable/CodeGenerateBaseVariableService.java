package net.lab1024.sa.common.module.support.codegenerator.service.variable;

import com.google.common.base.CaseFormat;
import net.lab1024.sa.common.common.util.SmartStringUtil;
import net.lab1024.sa.common.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import net.lab1024.sa.common.module.support.codegenerator.domain.model.CodeField;
import net.lab1024.sa.common.module.support.codegenerator.domain.model.CodeInsertAndUpdate;
import net.lab1024.sa.common.module.support.codegenerator.domain.model.CodeInsertAndUpdateField;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/9/29 17:20:41
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
public abstract class CodeGenerateBaseVariableService {

    public abstract Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form);

    /**
     * 是否支持 :
     * 1、增加、修改
     * 2、删除
     *
     * @param form
     * @return
     */
    public abstract boolean isSupport(CodeGeneratorConfigForm form);

    /**
     * 获取所有javabean的 import 包名
     *
     * @param form
     * @return
     */
    public List<String> getJavaBeanImportClass(CodeGeneratorConfigForm form) {
        String upperCamelName = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_CAMEL, form.getBasic().getModuleName());
        ArrayList<String> list = new ArrayList<>();

        list.add("import " + form.getBasic().getJavaPackageName() + ".domain.entity." + upperCamelName + "Entity;" );

        list.add("import " + form.getBasic().getJavaPackageName() + ".domain.form." + upperCamelName + "AddForm;" );
        list.add("import " + form.getBasic().getJavaPackageName() + ".domain.form." + upperCamelName + "UpdateForm;" );
        list.add("import " + form.getBasic().getJavaPackageName() + ".domain.form." + upperCamelName + "QueryForm;" );

        list.add("import " + form.getBasic().getJavaPackageName() + ".domain.vo." + upperCamelName + "VO;" );
        return list;
    }


    /**
     * 根据列名查找 CodeField
     */
    public CodeField getCodeFieldByColumnName(String columnName, CodeGeneratorConfigForm form) {
        List<CodeField> fields = form.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }

        return fields.stream().filter(e -> columnName.equals(e.getColumnName()))
                .findFirst().get();
    }


    /**
     * 是否为文件上传字段
     */
    protected boolean isFile(String columnName, CodeGeneratorConfigForm form) {
        CodeInsertAndUpdate insertAndUpdate = form.getInsertAndUpdate();
        if (insertAndUpdate == null) {
            return false;
        }

        List<CodeInsertAndUpdateField> fieldList = insertAndUpdate.getFieldList();
        if (CollectionUtils.isEmpty(fieldList)) {
            return false;
        }

        Optional<CodeInsertAndUpdateField> first = fieldList.stream().filter(e -> columnName.equals(e.getColumnName())).findFirst();
        if (!first.isPresent()) {
            return false;
        }

        CodeInsertAndUpdateField field = first.get();
        return SmartStringUtil.contains(field.getFrontComponent(), "Upload" );
    }

    /**
     * 是否为 枚举
     */
    protected boolean isDict(String columnName, CodeGeneratorConfigForm form) {
        List<CodeField> fields = form.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return false;
        }

        Optional<CodeField> first = fields.stream().filter(e -> columnName.equals(e.getColumnName())).findFirst();
        if (first.isPresent()) {
            return false;
        }

        CodeField codeField = first.get();
        return codeField.getDict() != null;
    }

    /**
     * 是否为 枚举
     */
    protected boolean isEnum(String columnName, CodeGeneratorConfigForm form) {
        List<CodeField> fields = form.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return false;
        }

        Optional<CodeField> first = fields.stream().filter(e -> columnName.equals(e.getColumnName())).findFirst();
        if (first.isPresent()) {
            return false;
        }

        CodeField codeField = first.get();
        return codeField.getEnumName() != null;
    }

    /**
     * 获取字段集合
     *
     * @param form
     * @return
     */
    protected Map<String, CodeField> getFieldMap(CodeGeneratorConfigForm form) {
        List<CodeField> fields = form.getFields();
        if (fields == null) {
            return new HashMap<>();
        }

        return fields.stream().collect(Collectors.toMap(CodeField::getColumnName, Function.identity()));
    }

    /**
     * 获取java类型
     *
     * @return
     */
    protected String getJavaPackageName(String javaType) {
        if ("BigDecimal".equals(javaType)) {
            return "import java.math.BigDecimal";
        } else if ("LocalDate".equals(javaType)) {
            return "import java.time.LocalDate;";
        } else if ("LocalDateTime".equals(javaType)) {
            return "import java.time.LocalDateTime;";
        } else {
            return null;
        }
    }

}
