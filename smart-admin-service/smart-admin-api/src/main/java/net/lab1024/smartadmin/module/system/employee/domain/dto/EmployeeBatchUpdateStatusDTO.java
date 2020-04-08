package net.lab1024.smartadmin.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量更新
 *
 * @author lidoudou
 * @date 2017年12月21日上午13:17:52
 */
@Data
public class EmployeeBatchUpdateStatusDTO {

    @ApiModelProperty("员工ids")
    @NotNull(message = "员工ids不能为空")
    private List<Long> employeeIds;

    @ApiModelProperty("状态")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
