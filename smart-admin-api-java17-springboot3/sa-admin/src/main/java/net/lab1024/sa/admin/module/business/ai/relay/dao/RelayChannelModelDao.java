package net.lab1024.sa.admin.module.business.ai.relay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayChannelModelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RelayChannelModelDao extends BaseMapper<RelayChannelModelEntity> {

    List<RelayChannelModelEntity> selectByChannelId(@Param("channelId") Long channelId);

    void deleteByChannelId(@Param("channelId") Long channelId);
}
