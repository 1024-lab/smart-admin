package net.lab1024.sa.base.common.util;

import cn.idev.excel.FastExcel;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {


    public static <T> List<T> importExcelByClass(MultipartFile file, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            FastExcel.read(file.getInputStream(), clazz, new AnalysisEventListener<T>() {

                @Override
                public void invoke(T data, AnalysisContext context) {
                    list.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {

                }
            }).doReadAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
