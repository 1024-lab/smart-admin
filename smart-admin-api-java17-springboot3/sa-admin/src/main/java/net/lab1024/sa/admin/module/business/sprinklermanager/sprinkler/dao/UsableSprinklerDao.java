package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 可用仓喷头
 *
 * @Author 海印: 芦苇
 */
@Mapper
@Component
public interface UsableSprinklerDao extends BaseMapper<UsableSprinklerEntity> {



}
