package net.lab1024.sa.admin.module.business.oa.enterprise.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.sa.common.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 查询企业员工
 *
 * @Author 1024创新实验室: 开云
 * @Date 2021-12-20 21:06:49
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class EnterpriseEmployeeQueryForm extends PageParam {

    @ApiModelProperty("搜索词")
    @Length(max = 20, message = "搜索词最多20字符")
    private String keyword;

    @ApiModelProperty("公司Id")
    @NotNull(message = "公司id 不能为空")
    private Long enterpriseId;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;

}
