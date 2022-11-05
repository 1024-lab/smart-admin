package net.lab1024.sa.admin.module.business.oa.enterprise.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * OA企业模块编辑
 *
 * @Author 1024创新实验室: 开云
 * @Date 2022/7/28 20:37:15
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Data
public class EnterpriseUpdateForm extends EnterpriseCreateForm {

    @ApiModelProperty("企业ID")
    @NotNull(message = "企业ID不能为空")
    private Long enterpriseId;
}
