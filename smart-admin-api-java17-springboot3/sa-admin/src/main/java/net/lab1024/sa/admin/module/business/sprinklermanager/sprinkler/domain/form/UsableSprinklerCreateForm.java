package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class UsableSprinklerCreateForm {

    @Schema(description = "喷头序列号")
    @NotBlank(message = "喷头序列号不能为空")
    @Length(max = 20, message = "sprinklerSerial最多20字符")
    private String sprinklerSerial;

    @Schema(description = "返可用仓日期")
    private LocalDate returnUsableRepoDate;

    @Schema(description = "历史")
    private String history;

    @Schema(description = "领用是否有限制")
    private Boolean isLimitedAllocation;

    @Schema(description = "领用时备注1")
    private String allocationNote1;

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
