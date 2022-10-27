package net.lab1024.sa.admin.module.system.department.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeVO;

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
public class DepartmentEmployeeTreeVO extends DepartmentVO {

    @ApiModelProperty("部门员工列表")
    private List<EmployeeVO> employees;

    @ApiModelProperty("子部门")
    private List<DepartmentEmployeeTreeVO> children;

}
