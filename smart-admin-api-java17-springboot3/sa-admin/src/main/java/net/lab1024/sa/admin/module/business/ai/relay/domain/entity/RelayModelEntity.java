package net.lab1024.sa.admin.module.business.ai.relay.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_ai_model")
public class RelayModelEntity {

    @TableId(value = "model_id", type = IdType.AUTO)
    private Long modelId;

    private String modelName;

    private Integer endpointType;

    private BigDecimal inputPricePerK;

    private BigDecimal outputPricePerK;

    private Integer disabledFlag;

    private Integer deletedFlag;

    private Long createdBy;

    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
