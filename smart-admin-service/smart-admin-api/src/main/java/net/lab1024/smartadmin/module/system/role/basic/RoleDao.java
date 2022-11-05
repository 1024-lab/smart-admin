package net.lab1024.smartadmin.module.system.role.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.smartadmin.module.system.role.basic.domain.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/3/27 0027 下午 13:00
 * @since JDK1.8
 */
@Mapper
@Component
public interface RoleDao extends BaseMapper<RoleEntity> {


    RoleEntity getByRoleName(@Param("roleName") String name);

}
