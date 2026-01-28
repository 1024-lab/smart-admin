package net.lab1024.sa.admin.module.business.ai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 消息表 列表VO
 *
 * @Author Mission
 * @Date 2025-12-21 16:10:16
 * @Copyright Mission
 */

@Data
public class AiMessagesVO {


    @Schema(description = "id")
    private String id;

    @Schema(description = "会话ID")
    private String conversatonId;

    @Schema(description = "查询")
    private String query;

    @Schema(description = "消息")
    private String message;

    @Schema(description = "入参")
    private String inputs;

    @Schema(description = "回答")
    private String answer;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建人姓名")
    private String createByName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新人姓名")
    private String updateByName;

    @Schema(description = "更新时间")
    private LocalDateTime updateAt;

}
