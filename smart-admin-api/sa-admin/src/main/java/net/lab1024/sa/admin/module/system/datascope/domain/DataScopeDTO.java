package net.lab1024.sa.admin.module.system.datascope.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 数据范围
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020/11/28  20:59:17
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Builder
public class DataScopeDTO {

    @Schema(description = "数据范围类型")
    private Integer dataScopeType;

    @Schema(description = "数据范围名称")
    private String dataScopeTypeName;

    @Schema(description = "描述")
    private String dataScopeTypeDesc;

    @Schema(description = "顺序")
    private Integer dataScopeTypeSort;

}
