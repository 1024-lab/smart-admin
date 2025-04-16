package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 喷头信息
 * @Author 海印: 芦苇
 */
@Data
public class SprinklerStockInVO {

    @Schema(description = "喷头ID")
    private Long sprinklerId;

    @DataTracerFieldLabel("购入日期（合同编号）")
    private String purchaseDateContractNumber;

    @DataTracerFieldLabel("喷头型号")
    private String sprinklerModel;

    @DataTracerFieldLabel("喷头序列号")
    private String sprinklerSerial;

    @DataTracerFieldLabel("发货日期")
    private LocalDate shippingDate;

    @DataTracerFieldLabel("入仓日期")
    private LocalDate warehouseDate;

    @DataTracerFieldLabel("电压")
    private Float voltage;

    @DataTracerFieldLabel("jetsout")
    private Byte jetsout;

    @DataTracerFieldLabel("history")
    private String history;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "创建人ID")
    private Long createUserId;

    @Schema(description = "创建人名称")
    private String createUserName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
