package com.gangquan360.smartadmin.module.codegenerator.service;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/5/11 0011 上午 8:33
 * @since JDK1.8
 */
@Component
public class CodeGeneratorComponent {

    private static Map<String,String> dataMap = new HashMap<>(16);

    static {
       dataMap();
    }

    public static void dataMap(){
        dataMap.put("int","Integer");
        dataMap.put("tinyint","Integer");
        dataMap.put("smallint","Integer");
        dataMap.put("integer","Integer");
        dataMap.put("bigint","Long");
        dataMap.put("float","Float");
        dataMap.put("double","Double");
        dataMap.put("decimal","BigDecimal");

        dataMap.put("char","String");
        dataMap.put("varchar","String");
        dataMap.put("tinytext","String");
        dataMap.put("text","String");
        dataMap.put("longtext","String");

        dataMap.put("date","Date");
        dataMap.put("datetime","Date");
        dataMap.put("timestamp","Date");

    }


    public Map<String,String> codeTemplates(String moduleClass, String basePackage, String modulePackage){
        String javaPackagePath = "src"+File.separator+"main" + File.separator + "java" + File.separator;
        javaPackagePath = javaPackagePath + basePackage.replace(".", File.separator) + File.separator+"module"+File.separator + modulePackage + File.separator;
        String xmlPackagePath = "src"+File.separator+"main" + File.separator + "resources" + File.separator +"mapper"+File.separator+ modulePackage + File.separator;
        Map<String,String> templateMap = new HashMap<>(7);
        templateMap.put("templates/codegenerator/Controller.java.vm",javaPackagePath+"controller"+File.separator+moduleClass+"Controller.java");
        templateMap.put("templates/codegenerator/Dao.java.vm",javaPackagePath+"dao"+File.separator+moduleClass+"Dao.java");
        templateMap.put("templates/codegenerator/Dao.xml.vm",xmlPackagePath+moduleClass+"Mapper.xml");
        templateMap.put("templates/codegenerator/DTO.java.vm",javaPackagePath+"domain"+File.separator+"dto"+File.separator+moduleClass+"DTO.java");
        templateMap.put("templates/codegenerator/Entity.java.vm",javaPackagePath+"domain"+File.separator+"entity"+File.separator+moduleClass+"Entity.java");
        templateMap.put("templates/codegenerator/QueryDTO.java.vm",javaPackagePath+"domain"+File.separator+"dto"+File.separator+moduleClass+"QueryDTO.java");
        templateMap.put("templates/codegenerator/Service.java.vm",javaPackagePath+"service"+File.separator+moduleClass+"Service.java");
        return templateMap;
    }

    public String getJavaType(String mysqlType ){
        String javaType = dataMap.get(mysqlType);
        return javaType;
    }


}
