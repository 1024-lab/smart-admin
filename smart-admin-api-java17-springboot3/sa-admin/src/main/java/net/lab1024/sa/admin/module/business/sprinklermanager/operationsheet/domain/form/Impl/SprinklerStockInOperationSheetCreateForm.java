package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.BaseForm;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 喷头-喷头入库记录信息新建
 *
 * @Author 海印:芦苇
 */
@Data
public class SprinklerStockInOperationSheetCreateForm implements BaseForm {

    @Schema(description = "喷头日期")
    private LocalDate purchaseDate;

    @Schema(description = "合同编号")
    @Length(max = 20, message = "合同编号最多20字符")
    private String contractNumber;

    @Schema(description = "喷头Id")
    @NotNull(message = "喷头Id不能为空")
    private Long sprinklerId;

    @Schema(description = "喷头Id")
    @NotNull(message = "喷头Id不能为空")
    private Long operationSheetId;

    @Schema(description = "喷头序列号")
    @NotBlank(message = "喷头序列号不能为空")
    @Length(max = 20, message = "喷头序列号最多20字符")
    private String sprinklerSerial;

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
