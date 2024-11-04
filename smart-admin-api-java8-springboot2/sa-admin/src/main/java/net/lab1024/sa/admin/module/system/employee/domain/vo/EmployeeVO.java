package net.lab1024.sa.admin.module.system.employee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.enumeration.GenderEnum;
import net.lab1024.sa.base.common.swagger.SchemaEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工信息
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021-12-21 23:05:56
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class EmployeeVO {

    @Schema(description = "主键id")
    private Long employeeId;

    @Schema(description = "登录账号")
    private String loginName;

    @SchemaEnum(GenderEnum.class)
    private Integer gender;

    @Schema(description = "员工名称")
    private String actualName;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "部门id")
    private Long departmentId;

    @Schema(description = "是否被禁用")
    private Boolean disabledFlag;

    @Schema(description = "是否 超级管理员")
    private Boolean administratorFlag;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "角色列表")
    private List<Long> roleIdList;

    @Schema(description = "角色名称列表")
    private List<String> roleNameList;

    @Schema(description = "职务ID")
    private Long positionId;

    @Schema(description = "职务名称")
    private String positionName;

    @Schema(description = "邮箱")
    private String email;

}
