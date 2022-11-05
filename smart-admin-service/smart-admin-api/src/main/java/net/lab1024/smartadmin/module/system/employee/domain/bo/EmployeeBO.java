package net.lab1024.smartadmin.module.system.employee.domain.bo;

import net.lab1024.smartadmin.module.system.employee.domain.entity.EmployeeEntity;
import lombok.Getter;


@Getter
public class EmployeeBO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 员工名称
     */
    private String actualName;

    /**
     * 别名
     */
    private String nickName;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 是否离职
     */
    private Integer isLeave;

    /**
     * 是否被禁用
     */
    private Integer isDisabled;

    /**
     * 删除状态 0否 1是
     */
    private Long isDelete;

    /**
     * 是否为超级管理员
     */
    private Boolean isSuperman;

    public EmployeeBO(EmployeeEntity employeeEntity, boolean isSuperman) {
        this.id = employeeEntity.getId();
        this.loginName = employeeEntity.getLoginName();
        this.actualName = employeeEntity.getActualName();
        this.nickName = employeeEntity.getNickName();
        this.phone = employeeEntity.getPhone();
        this.departmentId = employeeEntity.getDepartmentId();
        this.isLeave = employeeEntity.getIsLeave();
        this.isDisabled = employeeEntity.getIsDisabled();
        this.isDelete = employeeEntity.getIsDelete();
        this.isSuperman = isSuperman;
    }

}
