package net.lab1024.sa.admin.module.business.ai.relay.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class RelayTokenQueryForm extends PageParam {

    @Schema(description = "员工ID")
    private Long employeeId;

    @Schema(description = "渠道组ID")
    private Long channelGroupId;

    @Schema(description = "Token名称")
    private String tokenName;

    @Schema(description = "是否禁用")
    private Integer disabledFlag;
}
