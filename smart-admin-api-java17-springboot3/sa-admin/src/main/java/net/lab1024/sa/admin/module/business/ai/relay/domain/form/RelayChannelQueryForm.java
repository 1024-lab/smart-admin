package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class RelayChannelQueryForm extends PageParam {

    @Schema(description = "渠道名称")
    private String channelName;

    @Schema(description = "供应商类型")
    private String providerType;

    @Schema(description = "是否禁用")
    private Integer disabledFlag;
}
