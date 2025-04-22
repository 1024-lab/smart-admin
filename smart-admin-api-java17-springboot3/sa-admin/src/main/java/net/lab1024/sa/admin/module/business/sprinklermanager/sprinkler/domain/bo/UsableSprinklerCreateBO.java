package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.bo;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class UsableSprinklerCreateBO {

    @ExcelProperty("喷头序列号")
    @Schema(description = "喷头序列号")
    @NotBlank(message = "喷头序列号不能为空")
    @Length(max = 20, message = "喷头序列号最多20字符")
    private String sprinklerSerial;

    @ExcelProperty("返可用仓日期")
    @Schema(description = "返可用仓日期")
    private LocalDate returnUsableRepoDate;

    @ExcelProperty("历史")
    @Schema(description = "history")
    private String history;

    @ExcelProperty("领用是否有限制")
    @Schema(description = "领用是否有限制")
    private Boolean isLimitedAllocation;

    @ExcelProperty("领用时备注1")
    @Schema(description = "领用时备注1")
    private String allocationNote1;

    @ExcelProperty("所在仓status")
    @Schema(description = "所在仓status")
    private Byte status;


}
