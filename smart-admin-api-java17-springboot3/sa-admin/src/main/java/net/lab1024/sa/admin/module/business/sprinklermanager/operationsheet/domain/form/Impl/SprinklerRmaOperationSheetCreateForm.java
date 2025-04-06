package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.BaseForm;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 喷头管理-喷头Rma记录信息新建
 *
 * @Author 海印:芦苇
 */
@Data
public class SprinklerRmaOperationSheetCreateForm implements BaseForm {

    @Schema(description = "喷头Id")
    @NotNull(message = "喷头Id不能为空")
    private Long sprinklerId;

    @Schema(description = "喷头记录Id")
    @NotNull(message = "喷头记录Id不能为空")
    private Long operationSheetId;

    @Schema(description = "喷头序列号")
    @NotBlank(message = "喷头序列号不能为空")
    @Length(max = 20, message = "喷头序列号最多20字符")
    private String sprinklerSerial;

    @Schema(description = "快递日期")
    private LocalDate rmaDate;

    @Schema(description = "rma号")
    @Length(max = 20, message = "rma号最多20字符")
    private String rmaNumber;

    @Schema(description = "寄件地点")
    @Length(max = 20, message = "寄件地点最多20字符")
    private String rmaPosition;

    @Schema(description = "rma原因")
    @Length(max = 20, message = "rma原因最多20字符")
    private String rmaReason;

    @Schema(description = "快递单号")
    @Length(max = 20, message = "快递单号最多20字符")
    private String postNumber;

    @Schema(description = "处理结果")
    private String processResult;

    @Schema(description = "是否换新")
    @Length(max = 20, message = "是否换新最多20字符")
    private String agreeReplacement;

    @Schema(description = "换回")
    private String replacementResult;

    @Schema(description = "算不算在维修仓")
    @Length(max = 20, message = "是否换新最多20字符")
    private String inMaintainWarehouse;

    @Schema(description = "返修原因")
    @Length(max = 20, message = "返修原因最多20字符")
    private String maintainReason;

    @Schema(description = "返修日期")
    private LocalDate maintainDate;

    @Schema(description = "返修客户")
    @Length(max = 20, message = "返修客户最多20字符")
    private String maintainUser;

    @Schema(description = "历史")
    private String history;

    @Schema(description = "禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @Schema(hidden = true)
    private Long createUserId;

    @Schema(hidden = true)
    private String createUserName;

    @Override
    public Long getId() {
        return this.getSprinklerId();
    }
}
