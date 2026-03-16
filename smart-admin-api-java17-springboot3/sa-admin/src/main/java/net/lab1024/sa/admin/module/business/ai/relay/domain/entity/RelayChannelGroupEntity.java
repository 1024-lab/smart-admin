package net.lab1024.sa.admin.module.business.ai.relay.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_ai_channel_group")
public class RelayChannelGroupEntity {

    @TableId(value = "channel_group_id", type = IdType.ASSIGN_ID)
    private Long channelGroupId;

    private String channelName;

    private String channelIds;

    private Integer disabledFlag;

    private Integer deletedFlag;

    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
