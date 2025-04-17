package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class SprinklerCreateForm {

    @Schema(description = "购入日期（合同编号）")
    @Length(max = 50, message = "购入日期（合同编号）最多50字符")
    private String purchaseDateContractNumber;

    @Schema(description = "喷头型号")
    @Length(max = 50, message = "喷头型号最多50字符")
    private String sprinklerModel;

    @Schema(description = "喷头序列号")
    @NotBlank(message = "喷头序列号不能为空")
    @Length(max = 20, message = "sprinklerSerial最多20字符")
    private String sprinklerSerial;

    @Schema(description = "发货日期")
    private LocalDate shippingDate;

    @Schema(description = "入仓日期")
    private LocalDate warehouseDate;

    @Schema(description = "电压")
    private Float voltage;

    @Schema(description = "jetsout")
    private Byte jetsout;

    @Schema(description = "历史")
    private String history;

    @Schema(description = "所在仓status")
    private Byte status;

    @Schema(description = "禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @Schema(description = "创建人", hidden = true)
    private Long createUserId;

    @Schema(description = "创建人", hidden = true)
    private String createUserName;

}
