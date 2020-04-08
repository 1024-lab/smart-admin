package net.lab1024.smartadmin.module.business.log.orderoperatelog.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author bhr
 * @Description: 操作日志
 * @date 2019/8/28 9:31
 */
@Data
public class OrderOperateLogVO {

    private Long id;
    /**
     * 各种单据的id
     */
    @ApiModelProperty("各种单据的id")
    private Long orderId;
    /**
     * 单据类型
     */
    @ApiModelProperty("单据类型")
    private Integer orderType;
    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    private Integer operateType;
    /**
     * 操作类型 对应的中文
     */
    @ApiModelProperty("操作类型 对应的中文")
    private String operateContent;
    /**
     * 操作备注
     */
    @ApiModelProperty("操作备注")
    private String operateRemark;
    @ApiModelProperty("操作备注,包含审批人名使用别名显示")
    private String operateSecondRemark;
    /**
     * 员工id
     */
    @ApiModelProperty("员工id")
    private Long employeeId;
    /**
     * 员工名称
     */
    @ApiModelProperty("员工名称")
    private String employeeName;
    /**
     * 员工名称
     */
    @ApiModelProperty("员工别名")
    private String employeeSecondName;
    /**
     * 额外信息
     */
    @ApiModelProperty("额外信息")
    private String extData;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
}
