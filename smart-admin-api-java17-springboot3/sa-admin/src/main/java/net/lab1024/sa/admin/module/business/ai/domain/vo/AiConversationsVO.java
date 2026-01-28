package net.lab1024.sa.admin.module.business.ai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 会话表 列表VO
 *
 * @Author Mission
 * @Date 2025-12-21 16:08:28
 * @Copyright Mission
 */

@Data
public class AiConversationsVO {


    @Schema(description = "id")
    private String id;

    @Schema(description = "应用id")
    private String appId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "入参")
    private String inputs;

    @Schema(description = "总结")
    private String summary;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建人姓名")
    private String createByName;

    @Schema(description = "更新时间")
    private LocalDateTime updateAt;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新人姓名")
    private String updateByName;

}
