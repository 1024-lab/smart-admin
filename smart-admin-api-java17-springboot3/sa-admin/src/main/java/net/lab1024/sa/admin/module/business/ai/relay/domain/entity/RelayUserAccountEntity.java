package net.lab1024.sa.admin.module.business.ai.relay.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_ai_user_account")
public class RelayUserAccountEntity {

    @TableId(value = "employee_id")
    private Long employeeId;

    private BigDecimal quotaRemain;

    private BigDecimal quotaUsed;

    private Integer disabledFlag;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
