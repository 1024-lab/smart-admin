package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_operation_sheet")
public class OperationSheetEntity {

    @TableId(type = IdType.AUTO)
    private Long operationSheetId;

    /**
     * 操作的喷头Id
     */
    private Long sprinklerId;

    /**
     * 是否被禁用 0否1是
     */
    private Boolean disabledFlag;

    /**
     * 是否删除0否 1是
     */
    private Boolean deletedFlag;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
