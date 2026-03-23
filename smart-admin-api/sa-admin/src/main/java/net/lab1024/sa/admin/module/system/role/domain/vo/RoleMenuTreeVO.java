package net.lab1024.sa.admin.module.system.role.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.admin.module.system.menu.domain.vo.MenuSimpleTreeVO;

import java.util.List;

/**
 * 角色菜单树
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-04-08 21:53:04
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class RoleMenuTreeVO {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "菜单列表")
    private List<MenuSimpleTreeVO> menuTreeList;

    @Schema(description = "选中的菜单ID")
    private List<Long> selectedMenuId;
}
