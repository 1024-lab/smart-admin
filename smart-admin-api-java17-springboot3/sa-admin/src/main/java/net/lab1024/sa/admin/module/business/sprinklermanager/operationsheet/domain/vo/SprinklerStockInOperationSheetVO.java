package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 喷头入库信息
 *
 * @Author 海印:芦苇
 */
@Data
public class SprinklerStockInOperationSheetVO {
    private Long sprinklerOperationSheetId;

    @Schema(description = "喷头ID")
    private Long sprinklerId;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "操作记录")
    private Long operationSheetId;

    @Schema(description = "是否被禁用")
    private Boolean disabledFlag;
}
