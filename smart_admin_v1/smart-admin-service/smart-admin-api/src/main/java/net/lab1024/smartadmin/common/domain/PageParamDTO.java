package net.lab1024.smartadmin.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分页基础参数
 *
 * @author lihaifan
 * @Date Created in 2017/10/28 16:19
 */
@Data
public class PageParamDTO {

    @NotNull(message = "分页参数不能为空")
    @ApiModelProperty(value = "页码(不能为空)", example = "1")
    protected Integer pageNum;

    @NotNull(message = "每页数量不能为空")
    @ApiModelProperty(value = "每页数量(不能为空)", example = "10")
    @Max(value = 500, message = "每页最大为500")
    protected Integer pageSize;

    @ApiModelProperty("是否查询总条数")
    protected Boolean searchCount;

    @ApiModelProperty("排序")
    protected List<OrderItemDTO> orders;
}
