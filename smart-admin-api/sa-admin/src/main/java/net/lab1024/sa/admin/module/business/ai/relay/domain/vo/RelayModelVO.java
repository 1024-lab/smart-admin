package net.lab1024.sa.admin.module.business.ai.relay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RelayModelVO {

    @Schema(description = "模型ID")
    private Long modelId;

    @Schema(description = "模型名称")
    private String modelName;

    @Schema(description = "端点类型")
    private Integer endpointType;

    @Schema(description = "输入价格(每1k tokens)")
    private BigDecimal inputPricePerK;

    @Schema(description = "输出价格(每1k tokens)")
    private BigDecimal outputPricePerK;

    @Schema(description = "是否禁用")
    private Integer disabledFlag;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
