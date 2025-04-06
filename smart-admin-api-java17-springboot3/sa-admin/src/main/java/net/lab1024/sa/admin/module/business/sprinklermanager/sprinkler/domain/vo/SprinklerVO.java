package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 喷头信息
 * @Author 海印: 芦苇
 */
@Data
public class SprinklerVO {

    @TableId(type = IdType.AUTO)
    private Long sprinklerId;

    @DataTracerFieldLabel("喷头序列号")
    private String sprinklerSerial;

    @DataTracerFieldLabel("入仓日期")
    private LocalDate warehouseDate;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "创建人ID")
    private Long createUserId;

    @Schema(description = "创建人名称")
    private String createUserName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
