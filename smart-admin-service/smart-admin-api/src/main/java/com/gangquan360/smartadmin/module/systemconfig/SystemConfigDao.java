package com.gangquan360.smartadmin.module.systemconfig;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.gangquan360.smartadmin.module.systemconfig.domain.dto.SystemConfigQueryDTO;
import com.gangquan360.smartadmin.module.systemconfig.domain.entity.SystemConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统参数配置 t_system_config Dao层
 *
 * @author GHQ
 * @date 2017-12-23 14:25
 */
@Component
@Mapper
public interface SystemConfigDao extends BaseMapper<SystemConfigEntity> {

    /**
     * 查询所有系统配置（分页）
     *
     * @param page
     * @return
     */
    List<SystemConfigEntity> selectSystemSettingList(Pagination page, SystemConfigQueryDTO queryDTO);

    /**
     * 根据key查询获取数据
     *
     * @param key
     * @return
     */
    SystemConfigEntity getByKey(@Param("key") String key);

    /**
     * 根据key查询获取数据  排除掉某個id的数据
     * @param key
     * @param excludeId
     * @return
     */
    SystemConfigEntity getByKeyExcludeId(@Param("key") String key,@Param("excludeId") Long excludeId);
    /**
     * 查询所有系统配置
     *
     * @return
     */
    List<SystemConfigEntity> selectSystemSettingList();

    /**
     * 根据分组查询所有系统配置
     * @param group
     * @return
     */
    List<SystemConfigEntity> getListByGroup(String group);


    SystemConfigEntity  selectByKeyAndGroup(@Param("configKey") String configKey, @Param("group") String group);
}
