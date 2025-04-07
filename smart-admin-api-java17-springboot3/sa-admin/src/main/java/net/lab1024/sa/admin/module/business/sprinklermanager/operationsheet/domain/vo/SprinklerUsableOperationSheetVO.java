package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 喷头Rma信息
 *
 * @Author 海印:芦苇
 */
@Data
public class SprinklerUsableOperationSheetVO {
    private Long sprinklerOperationSheetId;

    @Schema(description = "喷头ID")
    private Long sprinklerId;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "喷头操作记录Id")
    private Long operationSheetId;

    @Schema(description = "历史")
    private String history;

    @Schema(description = "备注1")
    private String note1;

    @Schema(description = "备注2")
    private String note2;


    @Schema(description = "是否被禁用")
    private Boolean disabledFlag;
}
