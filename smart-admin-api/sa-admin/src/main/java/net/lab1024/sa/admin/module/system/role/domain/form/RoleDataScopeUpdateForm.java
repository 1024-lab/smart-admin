package net.lab1024.sa.admin.module.system.role.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
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
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class RoleDataScopeUpdateForm {

    @Schema(description = "角色id")
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    @Schema(description = "设置信息")
    @Valid
    private List<RoleUpdateDataScopeListFormItem> dataScopeItemList;


    @Data
    public static class RoleUpdateDataScopeListFormItem {

        @Schema(description = "数据范围类型")
        @NotNull(message = "数据范围类型不能为空")
        private Integer dataScopeType;

        @Schema(description = "可见范围")
        @NotNull(message = "可见范围不能为空")
        private Integer viewType;
    }

}
