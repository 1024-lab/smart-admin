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
public class SprinklerExcelVO {

    @ExcelProperty("喷头序列号")
    private String sprinklerSerial;

    @ExcelProperty("入仓日期")
    private LocalDate warehouseDate;


}
