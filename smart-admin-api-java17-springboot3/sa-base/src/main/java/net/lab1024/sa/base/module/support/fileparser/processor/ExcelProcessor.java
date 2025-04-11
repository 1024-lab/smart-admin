package net.lab1024.sa.base.module.support.fileparser.processor;

import cn.idev.excel.ExcelReader;
import cn.idev.excel.FastExcel;
import cn.idev.excel.read.listener.PageReadListener;
import lombok.Data;
import net.lab1024.sa.base.module.support.fileparser.domain.vo.OutputExcelVO;
import net.lab1024.sa.base.module.support.fileparser.validator.CValidator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class ExcelProcessor {

    //分桶存储数据,imp 线程安全集合
    private final List<OutputExcelVO> sortableData = Collections.synchronizedList(new ArrayList<>());
    private final List<OutputExcelVO> unsortableData = Collections.synchronizedList(new ArrayList<>());
    private final List<String> errors = Collections.synchronizedList(new ArrayList<>());

//    public class MyPageReadListener<T> implements ReadListener<T>{
//
//        public static int BATCH_COUNT = 1;
//        /**
//         * Temporary storage of data
//         */
//        private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
//        /**
//         * consumer
//         */
//        private final Consumer<List<T>> consumer;
//
//        /**
//         * Single handle the amount of data
//         */
//        private final int batchCount;
//
//        public MyPageReadListener(Consumer<List<T>> consumer) {
//            this(consumer, BATCH_COUNT);
//        }
//
//        public MyPageReadListener(Consumer<List<T>> consumer, int batchCount) {
//            this.consumer = consumer;
//            this.batchCount = batchCount;
//        }
//
//        @Override
//        public void invoke(T data, AnalysisContext context) {
////            ReadRowHolder
//            ReadRowHolder readRowHolder = context.readRowHolder();
//            Map cellMap = readRowHolder.getCellMap();
//            cachedDataList.add(data);
//            if (cachedDataList.size() >= batchCount) {
//                consumer.accept(cachedDataList);
//                cachedDataList = ListUtils.newArrayListWithExpectedSize(batchCount);
//            }
//        }
//
//        @Override
//        public void doAfterAllAnalysed(AnalysisContext context) {
//            if (CollectionUtils.isNotEmpty(cachedDataList)) {
//                consumer.accept(cachedDataList);
//            }
//        }
//    }


    public void process(InputStream stream) {
        ExcelReader reader = FastExcel.read(stream, OutputExcelVO.class,
                new PageReadListener<OutputExcelVO>(dataList->{
                    dataList.forEach(data->{
                        if(CValidator.isValid(data.getC())){
                            System.out.println(data.getC());
                        }

                        //imp 安全获取字段
                        String cValue = Optional.ofNullable(data.getC()).orElse("");

                        //imp 增强校验器防御逻辑
                        if(!CValidator.isValid(data.getC())){
                            errors.add("无效C值："+ (cValue.isEmpty()? "[空值]": cValue));
                            return;
                        }

                        //imp 校验排序能力时服用已校验值
                        if(CValidator.isSortable(cValue)){
                            sortableData.add(data);
                        }else {
                            unsortableData.add(data);
                        }
                    });
                }, 1000))
                .build();
        reader.readAll();
        reader.close();
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
