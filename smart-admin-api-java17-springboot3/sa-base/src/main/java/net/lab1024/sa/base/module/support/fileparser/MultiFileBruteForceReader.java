package net.lab1024.sa.base.module.support.fileparser;


import net.lab1024.sa.base.module.support.fileparser.reader.SimpleExcelReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MultiFileBruteForceReader {
    // 全局错误收集器
    private static final List<String> errors = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        File dir = new File("excels");
        String absolutePath = dir.getAbsolutePath();
        System.out.println("绝对路径: " + absolutePath);
        File output = new File("merged.xlsx");
        File errorLog = new File("errors.txt");

        // 暴力读取所有Excel
        List<Map<String, Object>> allData = readAllExcels(dir);
    }

    // 遍历目录读取所有Excel
    private static List<Map<String, Object>> readAllExcels(File dir) throws IOException {
        List<Map<String, Object>> result = new ArrayList<>();
        for (File file : getExcelFiles(dir)) {
            result.addAll(new SimpleExcelReader().readExcelWithRowNum(file));
//            try {
//                result.addAll(new SimpleExcelReader().readExcelWithRowNum(file));
//            } catch (Exception e) {
//                errors.add("文件读取失败: " + file.getName() + " | 错误: " + e.getMessage());
//            }
        }
        return result;
    }

    // 获取所有Excel文件（简单粗暴版）
    private static List<File> getExcelFiles(File dir) {
        return Arrays.asList(Objects.requireNonNull(dir.listFiles(
                file -> file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls")
        )));
    }


}
