package net.lab1024.sa.admin.module.system.department.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-12 20:37:48
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class DepartmentVO {

    @Schema(description = "部门id")
    private Long departmentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门负责人姓名")
    private String managerName;

    @Schema(description = "部门负责人id")
    private Long managerId;

    @Schema(description = "父级部门id")
    private Long parentId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
