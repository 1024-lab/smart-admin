package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 喷头Rma信息
 *
 * @Author 海印:芦苇
 */
@Data
public class SprinklerRmaOperationSheetVO {
    private Long sprinklerOperationSheetId;

    @Schema(description = "喷头ID")
    private Long sprinklerId;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "喷头操作记录Id")
    private Long operationSheetId;

    @Schema(description = "快递日期")
    private LocalDate rmaDate;

    @Schema(description = "rma号")
    private String rmaNumber;

    @Schema(description = "寄件地点")
    private String rmaPosition;

    @Schema(description = "rma原因")
    private String rmaReason;

    @Schema(description = "快递单号")
    private String postNumber;

    @Schema(description = "处理结果")
    private String processResult;

    @Schema(description = "是否换新")
    private String agreeReplacement;

    @Schema(description = "换回")
    private String replacementResult;

    @Schema(description = "算不算在维修仓")
    private String inMaintainWarehouse;

    @Schema(description = "返修原因")
    private String maintainReason;

    @Schema(description = "返修日期")
    private LocalDate maintainDate;

    @Schema(description = "返修客户")
    private String maintainUser;

    @Schema(description = "历史")
    private String history;


    @Schema(description = "是否被禁用")
    private Boolean disabledFlag;
}
