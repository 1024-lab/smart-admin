package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

/**
 * 查询喷头入库信息表
 *
 * @Author 海印: 芦苇
 */
@Data
public class SprinklerStockInOperationSheetQueryForm extends PageParam {
    @Schema(description = "搜索词")
    @Length(max = 20, message = "搜索词最多20字符")
    private String keyword;

    @Schema(description = "喷头Id")
    @NotNull(message = "喷头id 不能为空")
    private Long sprinklerId;

    @Schema(description = "删除标识", hidden = true)
    private Boolean deletedFlag;
}
