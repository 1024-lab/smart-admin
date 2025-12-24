package net.lab1024.sa.admin.module.business.ai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 应用提供商 列表VO
 *
 * @Author Mission
 * @Date 2025-12-21 16:06:19
 * @Copyright Mission
 */

@Data
public class AiAppProviderVO {


    @Schema(description = "id")
    private String id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "baseUrl")
    private String baseUrl;

    @Schema(description = "key")
    private String key;

    @Schema(description = "平台扩展配置(JSON格式)")
    private String configJson;

    @Schema(description = "创建时间")
    private LocalDateTime createAt;

    @Schema(description = "创建人名称")
    private String createByName;

    @Schema(description = "更新时间")
    private LocalDateTime updateAt;

    @Schema(description = "创建人名称")
    private String updateByName;

}
