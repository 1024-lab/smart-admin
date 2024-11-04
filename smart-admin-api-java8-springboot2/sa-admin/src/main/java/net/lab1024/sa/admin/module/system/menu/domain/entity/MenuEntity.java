package net.lab1024.sa.admin.module.system.menu.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.admin.module.system.menu.constant.MenuTypeEnum;

import java.time.LocalDateTime;

/**
 * 菜单 表
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-03-06 22:04:37
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName(value = "t_menu")
public class MenuEntity {

    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 类型
     *
     * @see MenuTypeEnum
     */
    private Integer menuType;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 是否为外链
     */
    private Boolean frameFlag;

    /**
     * 外链地址
     */
    private String frameUrl;

    /**
     * 是否缓存
     */
    private Boolean cacheFlag;

    /**
     * 显示状态
     */
    private Boolean visibleFlag;

    /**
     * 禁用状态
     */
    private Boolean disabledFlag;

    /**
     * 后端权限字符串
     */
    private String apiPerms;

    /**
     * 权限类型
     */
    private Integer permsType;

    /**
     * 前端权限字符串
     */
    private String webPerms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 功能点关联菜单ID
     */
    private Long contextMenuId;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    private Long updateUserId;
}
