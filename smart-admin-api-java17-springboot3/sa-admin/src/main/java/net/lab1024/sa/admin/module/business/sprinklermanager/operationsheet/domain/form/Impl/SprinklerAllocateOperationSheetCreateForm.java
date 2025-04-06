package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.BaseForm;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 喷头管理-喷头领用记录信息新建
 *
 * @Author 海印:芦苇
 */
@Data
public class SprinklerAllocateOperationSheetCreateForm implements BaseForm {

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

    @Schema(description = "领用日期")
    private LocalDate allocateDate;

    @Schema(description = "领用人")
    @Length(max = 20, message = "领用人最多20字符")
    private String allocateUser;

    @Schema(description = "领用机台")
    @Length(max = 20, message = "领用机台最多20字符")
    private String allocateMachine;

    @Schema(description = "颜色位置")
    @Length(max = 20, message = "领用机台最多20字符")
    private String colorPosition;

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
