package net.lab1024.sa.admin.module.system.department.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 部门
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-12 20:37:48
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class DepartmentTreeVO extends DepartmentVO {

    @ApiModelProperty("同级上一个元素id")
    private Long preId;

    @ApiModelProperty("同级下一个元素id")
    private Long nextId;

    @ApiModelProperty("子部门")
    private List<DepartmentTreeVO> children;

    @ApiModelProperty("自己和所有递归子部门的id集合")
    private List<Long> selfAndAllChildrenIdList;

}
