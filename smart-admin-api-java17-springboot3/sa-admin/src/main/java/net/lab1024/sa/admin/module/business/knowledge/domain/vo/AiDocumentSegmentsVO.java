package net.lab1024.sa.admin.module.business.knowledge.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 文档分块表 列表VO
 *
 * @Author Mission
 * @Date 2026-01-28 20:45:56
 * @Copyright Mission
 */

@Data
public class AiDocumentSegmentsVO {


    @Schema(description = "id")
    private Long id;

    @Schema(description = "知识库id")
    private Long knowledgeId;

    @Schema(description = "文档名称")
    private Long documentId;

    @Schema(description = "知识类型: DOCUMENT-文档, QA-问答")
    private String type;

    @Schema(description = "文档字数")
    private Integer wordCount;

    @Schema(description = "排序")
    private Integer position;

    @Schema(description = "分块内容")
    private String content;

    @Schema(description = "回答，QA类型才有")
    private String answer;

    @Schema(description = "状态 pending-待处理 processing处理中 completed成功 fail失败")
    private String status;

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
