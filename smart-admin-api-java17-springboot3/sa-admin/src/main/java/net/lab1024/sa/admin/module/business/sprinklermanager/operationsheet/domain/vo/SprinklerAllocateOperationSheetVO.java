package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;

/**
 * 喷头入库信息
 *
 * @Author 海印:芦苇
 */
@Data
public class SprinklerAllocateOperationSheetVO {
    private Long sprinklerOperationSheetId;

    @Schema(description = "喷头ID")
    private Long sprinklerId;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "喷头操作记录Id")
    private Long operationSheetId;

    @Schema(description = "领用日期")
    private LocalDate allocateDate;

    @Schema(description = "领用人")
    private String allocateUser;

    @Schema(description = "领用机台")
    private String allocateMachine;

    @Schema(description = "颜色位置")
    private String colorPosition;

    @Schema(description = "历史")
    private String history;


    @Schema(description = "是否被禁用")
    private Boolean disabledFlag;
}
