package net.lab1024.sa.base.module.support.fileparser.processor;

import cn.idev.excel.ExcelReader;
import cn.idev.excel.FastExcel;
import lombok.Data;
import net.lab1024.sa.base.module.support.fileparser.domain.vo.OutputExcelVO;
import net.lab1024.sa.base.module.support.fileparser.exception.ValidationException;
import net.lab1024.sa.base.module.support.fileparser.listener.ValidatingReadListener;
import net.lab1024.sa.base.module.support.fileparser.validator.CValidator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ExcelProcessor {

    //分桶存储数据,imp 线程安全集合
    private List<OutputExcelVO> sortableData = Collections.synchronizedList(new ArrayList<>());
    private List<OutputExcelVO> unsortableData = Collections.synchronizedList(new ArrayList<>());
    private final List<String> errors = Collections.synchronizedList(new ArrayList<>());



    public void process(InputStream stream) {
        ValidatingReadListener listener = new ValidatingReadListener();


        try (ExcelReader reader = FastExcel.read(stream, OutputExcelVO.class, listener).build()) {
            reader.readAll();
        }

        if(!listener.getErrors().isEmpty()){
            listener.getErrors().forEach(error->
                System.err.printf("第%d行[%s]错误: %s%n",
                        error.getRowNumber(),
                        error.getColumnIndex(),
                        error.getMessage()
                        )
            );
            throw new ValidationException("Excel 数据校验失败");
        }

        //数据分类处理
        Map<Boolean, List<OutputExcelVO>> partitionedData = listener.getValidData().stream().collect(Collectors.partitioningBy(data -> CValidator.isSortable(data.getC())));

        sortableData = partitionedData.get(true);
        unsortableData = partitionedData.get(false);

        if(!sortableData.isEmpty()){
            sortData();
        }
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
                            Integer.parseInt(p2[1])
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
