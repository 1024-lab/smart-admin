package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 喷头信息
 *
 * @Author 海印: 芦苇
 */
@Data
public class SprinklerStockInExcelVO {

    @ExcelProperty("喷头序列号")
    private String sprinklerSerial;

    @ExcelProperty("发货日期")
    private LocalDate shippingDate;

    @ExcelProperty("入仓日期")
    private LocalDate warehouseDate;

    @ExcelProperty("电压")
    private Float voltage;

    @ExcelProperty("jetsout")
    private Byte jetsout;

    @ExcelProperty("历史")
    private String history;


}
