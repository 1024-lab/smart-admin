package net.lab1024.smartadmin.util.excel;

/**
 * @author zhuoda
 * @Date 2020/8/10
 */

import net.lab1024.smartadmin.util.SmartFileUtil;

import java.io.*;

public class SmartExcelReader {

    public static SmartExcel openExcel(String filePath) throws IOException {
        SmartFileUtil.isFileExistThrowException(filePath);
        return new SmartExcel(new File(filePath).getCanonicalPath());
    }

    public static SmartExcel openExcel(File file) throws IOException {
        return new SmartExcel(file.getCanonicalPath());
    }

    public static SmartExcel openExcel(InputStream ins, SmartExcelFileType fileType) throws IOException {
        return new SmartExcel(ins, fileType);
    }

    public static void main(String[] args) throws Exception {
        SmartExcel smartExcel = openExcel(new FileInputStream(new File("F:/privilege.xlsx")), SmartExcelFileType.XLSX);
        System.out.println(smartExcel.getSheetList());
    }

}
