package net.lab1024.sa.common.common.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * excel 工具类
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2018/01/17 13:54
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
public class SmartEasyPoiExcelUtil {
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,
                                   String fileName, boolean isCreateHeader, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName,
                                   HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    }

    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, fileName, response);
    }

    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName,
                                      HttpServletResponse response, ExportParams exportParams) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        downloadExcel(fileName, workbook, response);
    }


    public static void downloadExcel(String fileName, Workbook workbook, HttpServletResponse response) {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        }
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        try {
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            log.error("", e);
        }
    }

    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        downloadExcel(fileName, workbook, response);
    }

    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            //throw new NormalException("模板不能为空");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (NoSuchElementException e) {
            // throw new NormalException("excel文件不能为空");
        } catch (Exception e) {
            //throw new NormalException(e.getMessage());
            System.out.println(e.getMessage());
        }
        return list;
    }
}
