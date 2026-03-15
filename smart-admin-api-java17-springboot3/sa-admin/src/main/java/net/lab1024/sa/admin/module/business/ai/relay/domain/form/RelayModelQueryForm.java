package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class RelayModelQueryForm extends PageParam {

    @Schema(description = "模型名称")
    private String modelName;

    @Schema(description = "端点类型")
    private Integer endpointType;

    @Schema(description = "是否禁用")
    private Integer disabledFlag;
}
