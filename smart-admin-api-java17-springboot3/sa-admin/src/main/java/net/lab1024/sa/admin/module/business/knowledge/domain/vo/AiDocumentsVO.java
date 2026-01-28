package net.lab1024.sa.admin.module.business.knowledge.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 文档列表 列表VO
 *
 * @Author Mission
 * @Date 2026-01-28 20:36:21
 * @Copyright Mission
 */

@Data
public class AiDocumentsVO {


    @Schema(description = "id")
    private String id;

    @Schema(description = "知识库id")
    private Long knowledgeId;

    @Schema(description = "文档名称")
    private String name;

    @Schema(description = "文档字数")
    private Integer wordCount;

    @Schema(description = "状态 pending-待处理 success-成功 fail-失败")
    private String status;

    @Schema(description = "文件id")
    private String fileId;

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
    private LocalDateTime updateTime;

}
