package net.lab1024.sa.common.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 分页基础参数
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2020/04/28 16:19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class PageParam {

    @ApiModelProperty(value = "页码(不能为空)", required = true, example = "1")
    @NotNull(message = "分页参数不能为空")
    private Integer pageNum;

    @ApiModelProperty(value = "每页数量(不能为空)", required = true, example = "10")
    @NotNull(message = "每页数量不能为空")
    @Max(value = 200, message = "每页最大为200")
    private Integer pageSize;

    @ApiModelProperty("是否查询总条数")
    protected Boolean searchCount;

    @ApiModelProperty("排序字段集合")
    @Size(max = 10, message = "排序字段最多10")
    @Valid
    private List<SortItem> sortItemList;

    /**
     * 排序DTO类
     */
    @Data
    public static class SortItem {

        @ApiModelProperty("true正序|false倒序")
        @NotNull(message = "排序规则不能为空")
        private Boolean isAsc;

        @ApiModelProperty(value = "排序字段")
        @NotBlank(message = "排序字段不能为空")
        @Length(max = 30, message = "排序字段最多30")
        private String column;
    }
}
