package net.lab1024.sa.base.module.support.message.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import net.lab1024.sa.base.common.swagger.SchemaEnum;
import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;
import net.lab1024.sa.base.module.support.message.constant.MessageTypeEnum;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 消息查询form
 *
 * @author luoyi
 * @date 2024/06/22 20:20
 */
@Data
public class MessageQueryForm extends PageParam {

    @Schema(description = "搜索词")
    @Length(max = 50, message = "搜索词最多50字符")
    private String searchWord;

    @SchemaEnum(value = MessageTypeEnum.class)
    @CheckEnum(value = MessageTypeEnum.class, message = "消息类型")
    private Integer messageType;

    @Schema(description = "是否已读")
    private Boolean readFlag;

    @Schema(description = "查询开始时间")
    private LocalDate startDate;

    @Schema(description = "查询结束时间")
    private LocalDate endDate;

    @Schema(hidden = true)
    private Long receiverUserId;

    @Schema(hidden = true)
    private Integer receiverUserType;
}
