package net.lab1024.sa.base.module.support.fileparser.wrapper;

import net.lab1024.sa.base.module.support.fileparser.domain.vo.OutputExcelVO;

import java.util.ArrayList;
import java.util.List;

public class DataWrapper {
    /**
     * 将 VO 对象转换为动态数据列表
     */
    public static List<List<Object>> wrapData(List<OutputExcelVO> dataList) {
        List<List<Object>> result = new ArrayList<>();

        for (OutputExcelVO vo : dataList) {
            List<Object> row = new ArrayList<>();
            for(int i=0;i<vo.getDynamicFields().size();i++){
                row.add(vo.getDynamicFields().get("A"+i));
            }
            result.add(row);
        }
        return result;
    }
}
