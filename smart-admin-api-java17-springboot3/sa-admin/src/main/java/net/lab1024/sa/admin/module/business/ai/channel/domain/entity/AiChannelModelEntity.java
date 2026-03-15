package net.lab1024.sa.admin.module.business.ai.channel.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_ai_channel_model")
public class AiChannelModelEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long channelId;

    private String modelName;
}
