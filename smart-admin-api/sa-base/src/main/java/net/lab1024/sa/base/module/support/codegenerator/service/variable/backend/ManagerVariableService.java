package net.lab1024.sa.base.module.support.codegenerator.service.variable.backend;

import net.lab1024.sa.base.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.model.CodeInsertAndUpdateField;
import net.lab1024.sa.base.module.support.codegenerator.service.variable.CodeGenerateBaseVariableService;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 1024创新实验室-主任:卓大
 * @Date 2022/9/29 17:20:41
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */

public class ManagerVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();

        List<CodeInsertAndUpdateField> updateFieldList = form.getInsertAndUpdate().getFieldList().stream().filter(e -> Boolean.TRUE.equals(e.getInsertFlag())).collect(Collectors.toList());
        List<String> packageList = getPackageList(updateFieldList, form);

        variablesMap.put("packageName", form.getBasic().getJavaPackageName() + ".manager" );
        variablesMap.put("importPackageList", packageList);

        return variablesMap;
    }


    public List<String> getPackageList(List<CodeInsertAndUpdateField> fields, CodeGeneratorConfigForm form) {
        if (CollectionUtils.isEmpty(fields)) {
            return new ArrayList<>();
        }

        HashSet<String> packageList = new HashSet<>();

        //1、javabean相关的包
        packageList.addAll(getJavaBeanImportClass(form).stream().filter(e -> e.contains("Entity;")).collect(Collectors.toList()));

        //2、dao
        packageList.add("import " + form.getBasic().getJavaPackageName() + ".dao."+ form.getBasic().getModuleName() + "Dao;" );
        return new ArrayList<>(packageList);
    }

}
