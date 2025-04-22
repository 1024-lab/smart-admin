package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.bo;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SprinklerCreateBO {
    @ExcelProperty("喷头序列号")
    @Schema(description = "喷头序列号")
    @NotBlank(message = "喷头序列号不能为空")
    @Length(max = 20, message = "sprinklerSerial最多20字符")
    private String sprinklerSerial;

    @ExcelProperty("所在仓status")
    @Schema(description = "所在仓status")
    private Byte status;


}
