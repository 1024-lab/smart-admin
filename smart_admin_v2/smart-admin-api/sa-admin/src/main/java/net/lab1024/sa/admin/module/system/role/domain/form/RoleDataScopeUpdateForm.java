package net.lab1024.sa.admin.module.system.role.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色的数据范围更新
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-04-08 21:53:04
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Data
public class RoleDataScopeUpdateForm {

    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    @ApiModelProperty("设置信息")
    @Valid
    private List<RoleUpdateDataScopeListFormItem> dataScopeItemList;


    @Data
    public static class RoleUpdateDataScopeListFormItem {

        @ApiModelProperty("数据范围类型")
        @NotNull(message = "数据范围类型不能为空")
        private Integer dataScopeType;

        @ApiModelProperty("可见范围")
        @NotNull(message = "可见范围不能为空")
        private Integer viewType;
    }

}
