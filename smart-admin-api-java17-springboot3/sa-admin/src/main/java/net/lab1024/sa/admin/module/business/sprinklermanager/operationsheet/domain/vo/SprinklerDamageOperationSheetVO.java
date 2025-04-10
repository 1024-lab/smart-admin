package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 喷头破损信息
 *
 * @Author 海印:芦苇
 */
@Data
public class SprinklerDamageOperationSheetVO {
    private Long sprinklerOperationSheetId;

    @Schema(description = "喷头ID")
    private Long sprinklerId;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "喷头操作记录Id")
    private Long operationSheetId;

    @Schema(description = "入破损仓日期")
    private LocalDate damageDate;

    @Schema(description = "历史")
    private String history;

    @Schema(description = "领用人")
    private String allocateUser;

    @Schema(description = "备注1")
    private String note1;

    @Schema(description = "破损原因分类")
    private String damageReason;

    @Schema(description = "具体原因")
    private String realReason;

    @Schema(description = "拆下好的过滤器")
    private String usableFilter;

    @Schema(description = "是否被禁用")
    private Boolean disabledFlag;
}
