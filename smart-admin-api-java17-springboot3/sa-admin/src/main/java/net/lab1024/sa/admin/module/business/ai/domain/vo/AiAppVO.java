package net.lab1024.sa.admin.module.business.ai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * ai应用管理表 列表VO
 *
 * @Author Mission
 * @Date 2025-12-21 16:00:44
 * @Copyright Mission
 */

@Data
public class AiAppVO {


    @Schema(description = "id")
    private String id;

    @Schema(description = "应用名称")
    private String name;

    @Schema(description = "应用描述")
    private String description;

    @Schema(description = "应用提供商id")
    private String appProviderId;

    @Schema(description = "应用级别配置(JSON格式)")
    private String appConfigJson;

    @Schema(description = "创建人姓名")
    private String createByName;

    @Schema(description = "创建时间")
    private LocalDateTime createAt;

    @Schema(description = "更新时间")
    private LocalDateTime updateAt;

    @Schema(description = "更新人姓名")
    private String updateByName;

}
