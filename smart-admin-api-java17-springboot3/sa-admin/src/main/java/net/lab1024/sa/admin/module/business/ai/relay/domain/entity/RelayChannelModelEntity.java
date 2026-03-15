package net.lab1024.sa.admin.module.business.ai.relay.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_ai_channel_model")
public class RelayChannelModelEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long channelId;

    private Long modelId;

    private Integer disabledFlag;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
