package net.lab1024.sa.base.module.support.fileparser.sorter;

import net.lab1024.sa.base.module.support.fileparser.domain.vo.OutputExcelVO;

import java.util.Comparator;
import java.util.List;

public class ExcelSorter {
    public static void sortByComplexField(List<OutputExcelVO> list) {
        list.sort(Comparator
                .comparingInt(ExcelSorter::getFirstPart)  // 主排序规则
                .thenComparingInt(ExcelSorter::getSecondPart) // 次级排序规则
        );
    }

    // 提取前半部分转换为整数
    private static int getFirstPart(OutputExcelVO vo) {
        try{
            String[] parts = vo.getDynamicFields().get("A2").toString().split("-");
            return Integer.parseInt(parts[0]);
        }catch (Exception e){
            System.out.println("machine_name:"+vo.getDynamicFields().get("machine_name")+" position"+vo.getDynamicFields().get("position"));
        }
        return 0;
    }

    // 提取后半部分转换为整数
    private static int getSecondPart(OutputExcelVO vo) {
        String[] parts = vo.getDynamicFields().get("A2").toString().split("-");
        return Integer.parseInt(parts[1]);
    }
}
