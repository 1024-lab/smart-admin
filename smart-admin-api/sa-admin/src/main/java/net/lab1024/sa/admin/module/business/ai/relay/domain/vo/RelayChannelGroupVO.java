package net.lab1024.sa.admin.module.business.ai.relay.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RelayChannelGroupVO {

    @Schema(description = "渠道组ID")
    private Long channelGroupId;

    @Schema(description = "渠道组名称")
    private String channelName;

    @JsonIgnore
    private String channelIds;

    @Schema(description = "渠道ID列表")
    private List<Long> channelIdList;

    @Schema(description = "渠道名称（逗号分隔）")
    private String channelNames;

    @Schema(description = "是否禁用")
    private Integer disabledFlag;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
