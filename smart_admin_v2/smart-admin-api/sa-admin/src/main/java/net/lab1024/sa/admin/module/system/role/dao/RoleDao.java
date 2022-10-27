package net.lab1024.sa.admin.module.system.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleEntity;

/**
 * 角色 dao
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-02-26 21:34:01
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Mapper
@Component
public interface RoleDao extends BaseMapper<RoleEntity> {

    /**
     * 根据角色名称查询
     * @param roleName
     * @return
     */
    RoleEntity getByRoleName(@Param("roleName") String roleName);

}
