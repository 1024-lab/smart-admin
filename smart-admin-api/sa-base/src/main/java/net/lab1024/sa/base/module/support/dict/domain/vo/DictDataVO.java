package net.lab1024.sa.base.module.support.dict.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典数据表 列表VO
 *
 * @Author 1024创新实验室-主任-卓大
 * @Date 2025-03-25 23:12:59
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */

@Data
public class DictDataVO implements Serializable {

    @Schema(description = "字典数据id")
    private Long dictDataId;

    @Schema(description = "字典id")
    private Long dictId;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "字典名字")
    private String dictName;

    @Schema(description = "字典禁用状态")
    private Boolean dictDisabledFlag;

    @Schema(description = "字典项值")
    private String dataValue;

    @Schema(description = "字典项显示名称")
    private String dataLabel;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序（越大越靠前）")
    private Integer sortOrder;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
