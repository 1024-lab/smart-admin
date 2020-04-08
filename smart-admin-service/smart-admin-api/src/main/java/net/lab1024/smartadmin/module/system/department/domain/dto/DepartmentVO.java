package net.lab1024.smartadmin.module.system.department.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.lab1024.smartadmin.module.system.employee.domain.dto.EmployeeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DepartmentVO {

    @ApiModelProperty("部门id")
    private Long id;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("部门简称")
    private String shortName;

    @ApiModelProperty("部门负责人姓名")
    private String managerName;

    @ApiModelProperty("部门负责人id")
    private Long managerId;

    @ApiModelProperty("子部门")
    @JsonProperty("children")
    private List<DepartmentVO> childrenDepartment;

    @ApiModelProperty("父级部门id")
    private Long parentId;

    @ApiModelProperty("同级上一个元素id")
    private Long preId;

    @ApiModelProperty("同级下一个元素id")
    private Long nextId;

    @ApiModelProperty("排序")
    private Long sort;

    @ApiModelProperty("父级部门名称")
    private String parentName;

    @ApiModelProperty("部门员工列表")
    private List<EmployeeDTO> employees;

    @ApiModelProperty("上次更新时间")
    private Date updateTime;

    @ApiModelProperty("创建时间")
    private Date createTime;


}
