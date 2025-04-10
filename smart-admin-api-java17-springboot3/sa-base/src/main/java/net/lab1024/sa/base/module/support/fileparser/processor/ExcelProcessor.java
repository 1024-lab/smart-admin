package net.lab1024.sa.base.module.support.fileparser.processor;

import cn.idev.excel.ExcelReader;
import cn.idev.excel.FastExcel;
import cn.idev.excel.read.listener.PageReadListener;
import lombok.Data;
import net.lab1024.sa.base.module.support.fileparser.domain.vo.OutputExcelVO;
import net.lab1024.sa.base.module.support.fileparser.validator.A2Validator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelProcessor {

    //分桶存储数据
    private final List<OutputExcelVO> sortableData = new ArrayList<>();
    private final List<OutputExcelVO> unsortableData = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();



    public void process(InputStream stream) {
        ExcelReader reader = FastExcel.read(stream, OutputExcelVO.class,
                new PageReadListener<OutputExcelVO>(dataList->{
                    dataList.forEach(data->{
                        if(!A2Validator.isValid(data.getC())){
                            errors.add("无效C值："+ data.getC());
                            return;
                        }

                        if(A2Validator.isSortable(data.getC())){
                            sortableData.add(data);
                        }else {
                            unsortableData.add(data);
                        }
                    });
                }, 1000)).build();
        reader.readAll();
        reader.close();

        sortData();
    }

    private void sortData(){
        sortableData.sort((d1, d2)->{
            String[] p1 = d1.getC().split("-");
            String[] p2 = d2.getC().split("-");

            int cmp1 = Integer.compare(
                    Integer.parseInt(p1[0]),
                    Integer.parseInt(p2[0])
            );
            return cmp1 != 0? cmp1:
                    Integer.compare(
                            Integer.parseInt(p1[1]),
                            Integer.parseInt(p2[2])
                    );
        });
    }

    //合并结果
    public List<OutputExcelVO> getMergedData() {
        List<OutputExcelVO> result = new ArrayList<>(sortableData);
        result.addAll(unsortableData);
        return result;
    }

}
