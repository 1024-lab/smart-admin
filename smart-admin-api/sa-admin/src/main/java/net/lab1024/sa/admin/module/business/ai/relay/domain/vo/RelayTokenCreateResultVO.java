package net.lab1024.sa.admin.module.business.ai.relay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Token创建结果，包含明文Token（仅创建时返回一次）
 */
@Data
public class RelayTokenCreateResultVO {

    @Schema(description = "Token ID")
    private Long tokenId;

    @Schema(description = "Token名称")
    private String tokenName;

    @Schema(description = "完整Token(仅此一次展示)")
    private String plainToken;

    @Schema(description = "Token前缀")
    private String tokenPrefix;
}
