package net.lab1024.sa.base.module.support.fileparser.reader;


import cn.idev.excel.ExcelReader;
import cn.idev.excel.FastExcel;
import net.lab1024.sa.base.module.support.fileparser.exception.ValidationException;
import net.lab1024.sa.base.module.support.fileparser.listener.MultiExcelReadListener;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// 增强版读取器（记录行号和文件名）
public class SimpleExcelReader {
    public List<Map<String, Object>> readExcelWithRowNum(File file) throws IOException {
        List<Map<String, Object>> data = new LinkedList<>();
        MultiExcelReadListener<LinkedHashMap<String, Object>> multiExcelReadListener = new MultiExcelReadListener<>();
        try (ExcelReader reader = FastExcel.read(file, LinkedHashMap.class, multiExcelReadListener).build()) {
            reader.readAll();
        }
        if(!multiExcelReadListener.getErrors().isEmpty()){
            multiExcelReadListener.getErrors().forEach(error->
                    System.err.printf("第%d行[%s]错误: %s%n",
                            error.getRowNumber(),
                            error.getColumnIndex(),
                            error.getMessage()
                    )
            );
            throw new ValidationException("Excel 数据校验失败");
        }

//        //数据分类处理
//        Map<Boolean, List<OutputExcelVO>> partitionedData = multiExcelReadListener.getRowData().get("data").stream().collect(Collectors.partitioningBy(data -> CValidator.isSortable(data.getC())));
//
//        sortableData = partitionedData.get(true);
//        unsortableData = partitionedData.get(false);
//
//        if(!sortableData.isEmpty()){
//            sortData();
//        }
        return data;
    }

    private String getHeaderByIndex(File file, int index) {
        return FastExcel.read(file)
                .headRowNumber(0)
                .doReadAllSync()
                .get(0)
//                .get(index)
                .toString();
    }
}
