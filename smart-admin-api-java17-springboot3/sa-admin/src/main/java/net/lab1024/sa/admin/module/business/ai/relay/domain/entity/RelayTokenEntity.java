package net.lab1024.sa.admin.module.business.ai.relay.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_ai_token")
public class RelayTokenEntity {

    @TableId(value = "token_id", type = IdType.AUTO)
    private Long tokenId;

    private Long employeeId;

    private Long channelGroupId;

    private String tokenName;

    private String tokenPrefix;

    private String tokenHash;

    private LocalDateTime expiresTime;

    private LocalDateTime lastUsedTime;

    private Long requestCount;

    private Integer disabledFlag;

    private Integer deletedFlag;

    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
