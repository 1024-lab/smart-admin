package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public class DamagedSprinklerCreateForm extends BaseCreateForm{

    @ExcelProperty("购入日期（合同编号）")
    @Schema(description = "购入日期（合同编号）")
    @Length(max = 50, message = "购入日期（合同编号）最多50字符")
    private String purchaseDateContractNumber;

    @ExcelProperty("喷头型号")
    @Schema(description = "喷头型号")
    @Length(max = 50, message = "喷头型号最多50字符")
    private String sprinklerModel;

    @ExcelProperty("喷头序列号")
    @Schema(description = "喷头序列号")
    @NotBlank(message = "喷头序列号不能为空")
    @Length(max = 20, message = "sprinklerSerial最多20字符")
    private String sprinklerSerial;

    @ExcelProperty("发货日期")
    @Schema(description = "发货日期")
    private LocalDate shippingDate;

    @ExcelProperty("入仓日期")
    @Schema(description = "入仓日期")
    private LocalDate warehouseDate;

    @ExcelProperty("电压")
    @Schema(description = "电压")
    private Float voltage;

    @ExcelProperty("jetsout")
    @Schema(description = "jetsout")
    private Byte jetsout;

    @ExcelProperty("历史")
    @Schema(description = "历史")
    private String history;

    @ExcelProperty("所在仓status")
    @Schema(description = "所在仓status")
    private Byte status;

    @ExcelIgnore
    @Schema(description = "禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ExcelIgnore
    @Schema(description = "创建人", hidden = true)
    private Long createUserId;

    @ExcelIgnore
    @Schema(description = "创建人", hidden = true)
    private String createUserName;
}
