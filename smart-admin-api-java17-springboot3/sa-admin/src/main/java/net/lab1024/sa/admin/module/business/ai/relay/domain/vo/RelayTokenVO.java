package net.lab1024.sa.admin.module.business.ai.relay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RelayTokenVO {

    @Schema(description = "Token ID")
    private Long tokenId;

    @Schema(description = "员工ID")
    private Long employeeId;

    @Schema(description = "Token名称")
    private String tokenName;

    @Schema(description = "Token前缀")
    private String tokenPrefix;

    @Schema(description = "过期时间")
    private LocalDateTime expiresTime;

    @Schema(description = "最后使用时间")
    private LocalDateTime lastUsedTime;

    @Schema(description = "请求次数")
    private Long requestCount;

    @Schema(description = "是否禁用")
    private Integer disabledFlag;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
