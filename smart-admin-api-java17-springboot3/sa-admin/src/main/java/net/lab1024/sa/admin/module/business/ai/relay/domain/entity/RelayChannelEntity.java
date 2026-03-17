package net.lab1024.sa.admin.module.business.ai.relay.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_ai_channel")
public class RelayChannelEntity {

    @TableId(value = "channel_id", type = IdType.ASSIGN_ID)
    private Long channelId;

    private String channelName;

    private String providerType;

    private String baseUrl;

    private String apiKeyCipher;

    private String apiKeyKid;

    private String apiKeyMask;

    private String organization;

    private Integer priority;

    private Integer weight;

    private Integer timeoutMs;

    private Integer maxRetries;

    private Integer disabledFlag;

    private Integer deletedFlag;

    private Long createdBy;

    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
