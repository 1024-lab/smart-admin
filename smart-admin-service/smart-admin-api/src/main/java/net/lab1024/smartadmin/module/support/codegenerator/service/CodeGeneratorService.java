package net.lab1024.smartadmin.module.support.codegenerator.service;

import com.google.common.base.CaseFormat;
import net.lab1024.smartadmin.module.support.codegenerator.dao.TableDao;
import net.lab1024.smartadmin.module.support.codegenerator.domain.CodeGeneratorDTO;
import net.lab1024.smartadmin.module.support.codegenerator.domain.CodeGeneratorQueryColumnDTO;
import net.lab1024.smartadmin.module.support.codegenerator.domain.ColumnVO;
import net.lab1024.smartadmin.module.support.codegenerator.domain.QueryFieldVO;
import net.lab1024.smartadmin.util.SmartDateUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.smartadmin.util.SmartStringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/5/11 0011 上午 9:36
 * @since JDK1.8
 */
@Slf4j
@Service
public class CodeGeneratorService {

    @Autowired
    private TableDao tableDao;

    @Autowired
    private CodeGeneratorComponent codeGeneratorComponent;

    /**
     * 大家注意了开始生成代码了
     *
     * @param codeGenerator
     * @throws Exception
     */
    public void codeGenerator(CodeGeneratorDTO codeGenerator) throws Exception {
        this.basicValid(codeGenerator);
        String date = SmartDateUtil.formatYMDHMS(new Date());
        String tableDesc = this.getTableDesc(codeGenerator.getTableName());
        String author = codeGenerator.getAuthor();
        String basePackage = codeGenerator.getBasePackage();
        if (StringUtils.isEmpty(basePackage)) {
            basePackage = "net.lab1024.smartadmin";
        }
        String moduleClass = this.tableName2Class(codeGenerator.getTableName(), codeGenerator.getTablePrefix());
        String moduleVar = this.tableName2Var(codeGenerator.getTableName(), codeGenerator.getTablePrefix());
        String modulePackage = codeGenerator.getModulePackage();

        List<ColumnVO> columnList = this.columnList(codeGenerator.getTableName());
        List<QueryFieldVO> queryFieldList = this.buildQueryField(codeGenerator, columnList);
        Map<String, String> codeTemplates = codeGeneratorComponent.codeTemplates(moduleClass, basePackage, modulePackage);
        List<String> queryImports = this.buildQueryImport(queryFieldList);
        List<String> dtoImports = this.buildDTOImport(columnList);
        List<String> entityImports = this.buildEntityImport(columnList);
        Properties p = new Properties();
        p.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.put("directive.foreach.counter.name", "velocityCount");
        p.put("directive.foreach.counter.initial.value", "1");
        Velocity.init(p);
        Map<String, Object> map = new HashMap<>();
        map.put("company", codeGenerator.getCompany());
        map.put("tableName", codeGenerator.getTableName());
        map.put("basePackage", basePackage);
        map.put("modulePackage", modulePackage);
        map.put("moduleClass", moduleClass);
        map.put("tableDesc", tableDesc);
        map.put("author", author);
        map.put("date", date);
        map.put("moduleVar", moduleVar);
        map.put("columnList", columnList);
        map.put("queryFieldList", queryFieldList);
        map.put("queryImports", queryImports);
        map.put("dtoImports", dtoImports);
        map.put("entityImports", entityImports);
        map.put("webModuleName", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, moduleClass).replaceAll("_", "-"));
        map.put("upperCamel", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, moduleClass));
        //前端的变量
        map.put("ViewUIMessage", "$Message");
        map.put("VueRefs", "$refs");
        VelocityContext context = new VelocityContext(map);
        this.codeGenerator(context, codeTemplates);
    }

    private List<String> buildQueryImport(List<QueryFieldVO> queryFieldList) {
        List<String> queryImports = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(queryFieldList)) {
            queryFieldList.forEach(e -> {
                importPackage(queryImports, e.getFieldType());
                if ("in".equals(e.getSqlOperate())) {
                    queryImports.add("import java.util.List;");
                }
            });
        }
        return queryImports;
    }

    private List<String> buildDTOImport(List<ColumnVO> columnList) {
        List<String> dtoImports = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(columnList)) {
            columnList.forEach(e -> {
                importPackage(dtoImports, e.getFieldType());
            });
        }
        return dtoImports;
    }

    private List<String> buildEntityImport(List<ColumnVO> columnList) {
        List<String> entityImports = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(columnList)) {
            columnList.forEach(e -> {
                if (!e.getFieldName().equals("createTime") && !e.getFieldName().equals("updateTime") && !e.getFieldName().equals("id")) {
                    importPackage(entityImports, e.getFieldType());
                }
            });
        }
        return entityImports;
    }

    private void importPackage(List<String> imports, String fieldType) {
        if ("Date".equals(fieldType) && !imports.contains("import java.util.Date;")) {
            imports.add("import java.util.Date;");
        }
        if ("BigDecimal".equals(fieldType) && !imports.contains("import java.math.BigDecimal;")) {
            imports.add("import java.math.BigDecimal;");
        }
    }

    private String getOutputDir() {
        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
        return desktopDir.getAbsolutePath() + File.separator + "smart-admin" + File.separator;
    }

    /**
     * 生成代码
     *
     * @param context
     * @param codeTemplates
     */
    private void codeGenerator(VelocityContext context, Map<String, String> codeTemplates) throws Exception {
        String projectPath = getOutputDir();

        Velocity.setProperty("input.encoding", "UTF-8");
        Velocity.setProperty("output.encoding", "UTF-8");

        for (Entry<String, String> entry : codeTemplates.entrySet()) {
            String template = entry.getKey();
            String filePath = projectPath + entry.getValue();
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
            String fileDir = filePath.replace(fileName, "");
            File directory = new File(fileDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            FileWriter writer;
            try {
                writer = new FileWriter(filePath);
                Template tpl = Velocity.getTemplate(template, "UTF-8");
                tpl.merge(context, writer);
                writer.flush();
                writer.close();
            } catch (Exception e) {
                log.error("", e);
            }
        }

        log.info("------------------------------ 代 码 生 成 完 毕 ！ ------------------------------");
        log.info("代码目录：{}", projectPath);
        log.info("------------------------------ 代 码 生 成 完 毕 ！ ------------------------------");

    }

    public static void main(String[] args) {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void basicValid(CodeGeneratorDTO codeGenerator) throws Exception {
        if (StringUtils.isEmpty(codeGenerator.getTableName())) {
            throw new Exception("你没建表吗?");
        }
        if (StringUtils.isEmpty(codeGenerator.getTablePrefix())) {
            throw new Exception("你的表没前缀吗?");
        }
        if (StringUtils.isEmpty(codeGenerator.getAuthor())) {
            throw new Exception("输入下你的大名");
        }
    }

    /**
     * 构建查询集合
     *
     * @param codeGenerator
     * @param columnList
     * @return
     * @throws Exception
     */
    private List<QueryFieldVO> buildQueryField(CodeGeneratorDTO codeGenerator, List<ColumnVO> columnList) throws Exception {
        List<QueryFieldVO> queryFieldList = Lists.newArrayList();
        Map<String, ColumnVO> storageMap = columnList.stream().collect(Collectors.toMap(ColumnVO::getColumnName, e -> e));
        List<CodeGeneratorQueryColumnDTO> queryColumnList = codeGenerator.getQueryColumnList();
        if (CollectionUtils.isEmpty(queryColumnList)) {
            return queryFieldList;
        }
        for (CodeGeneratorQueryColumnDTO queryColumn : queryColumnList) {
            ColumnVO columnDTO = storageMap.get(queryColumn.getColumnName());
            if (columnDTO == null) {
                String errorMsg = "sql列[" + queryColumn.getColumnName() + "]在表[" + codeGenerator.getTableName() + "]中不存在。";
                log.error(errorMsg);
                throw new Exception(errorMsg);
            }
            QueryFieldVO queryField =
                    QueryFieldVO.builder().fieldName(columnDTO.getFieldName()).fieldType(columnDTO.getFieldType()).columnName(columnDTO.getColumnName()).columnDesc(columnDTO.getColumnDesc()).sqlOperate(queryColumn.getSqlOperate().getName()).build();
            queryFieldList.add(queryField);
        }
        return queryFieldList;
    }

    /**
     * 列数据 组合
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    private List<ColumnVO> columnList(String tableName) throws Exception {
        List<ColumnVO> list = tableDao.selectTableColumn(tableName);
        for (ColumnVO column : list) {
            String javaType = codeGeneratorComponent.getJavaType(column.getColumnType());
            if (StringUtils.isEmpty(javaType)) {
                String errorMsg = "sql数据类型[" + column.getColumnType() + "]缺少对应的java类型。";
                log.error(errorMsg);
                throw new Exception(errorMsg);
            }
            if ("Integer".equals(javaType) && column.getColumnName().contains("id")) {
                column.setFieldType("Long");
            } else {
                column.setFieldType(javaType);
            }
            column.setFieldName(this.columnName2Field(column.getColumnName()));
            if (SmartStringUtil.isBlank(column.getColumnDesc())) {
                column.setColumnDesc(column.getColumnName());
            }

            column.setIsNumber(codeGeneratorComponent.isNumber(column.getColumnType()));
        }
        return list;
    }

    /**
     * 获取列注释为类描述
     *
     * @param tableName
     * @return
     */
    private String getTableDesc(String tableName) {
        return tableDao.selectTableDesc(tableName);
    }

    /**
     * 列名转字段名
     *
     * @param columnName
     * @return
     */
    private String columnName2Field(String columnName) {
        String transName = WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
        return WordUtils.uncapitalize(transName);
    }

    /**
     * 表名转类名前缀
     *
     * @param tableName
     * @param tablePrefix
     * @return
     */
    private String tableName2Class(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replaceFirst(tablePrefix, "");
        }
        return WordUtils.capitalizeFully(tableName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转包名
     *
     * @param tableName
     * @param tablePrefix
     * @return
     */
    private String tableName2Package(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replaceFirst(tablePrefix, "");
        }
        return tableName.replace("_", "");
    }

    /**
     * 表名转 java变量前缀
     *
     * @param tableName
     * @param tablePrefix
     * @return
     */
    private String tableName2Var(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replaceFirst(tablePrefix, "");
        }
        String transName = WordUtils.capitalizeFully(tableName, new char[]{'_'}).replace("_", "");
        return WordUtils.uncapitalize(transName);
    }

}
