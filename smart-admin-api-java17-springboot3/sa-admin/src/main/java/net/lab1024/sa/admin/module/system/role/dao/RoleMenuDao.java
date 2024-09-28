package net.lab1024.sa.admin.module.system.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.system.menu.domain.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleMenuEntity;

import java.util.List;

/**
 * 角色 菜单 dao
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-03-07 18:54:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
@Component
public interface RoleMenuDao extends BaseMapper<RoleMenuEntity> {

    /**
     * 根据角色ID删除菜单权限
     *
     */
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID查询选择的菜单权限
     *
     */
    List<Long> queryMenuIdByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID集合查询选择的菜单权限
     *
     */
    List<MenuEntity> selectMenuListByRoleIdList(@Param("roleIdList") List<Long> roleIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询所有的菜单角色
     *
     */
    List<RoleMenuEntity> queryAllRoleMenu();
}
