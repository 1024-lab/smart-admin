package net.lab1024.sa.admin.module.system.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleDataScopeEntity;

import java.util.List;


/**
 * 角色 数据权限 dao
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-02-26 21:34:01
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
@Component
public interface RoleDataScopeDao extends BaseMapper<RoleDataScopeEntity> {

    /**
     * 获取某个角色的设置信息
     */
    List<RoleDataScopeEntity> listByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取某批角色的所有数据范围配置信息
     */
    List<RoleDataScopeEntity> listByRoleIdList(@Param("roleIdList") List<Long> roleIdList);

    /**
     * 删除某个角色的设置信息
     */
    void deleteByRoleId(@Param("roleId") Long roleId);

}
