package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class RelayChannelGroupAddForm {

    @Schema(description = "渠道组名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "渠道组名称不能为空")
    private String channelName;

    @Schema(description = "渠道ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "渠道ID列表不能为空")
    private List<Long> channelIdList;

    @Schema(description = "备注")
    private String remark;
}
