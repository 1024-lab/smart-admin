package net.lab1024.sa.admin.module.business.ai.relay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayChannelEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayChannelVO;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface RelayChannelDao extends BaseMapper<RelayChannelEntity> {

    List<RelayChannelVO> queryPage(Page page, @Param("queryForm") RelayChannelQueryForm queryForm);

    List<RelayChannelEntity> selectEnabledByModelAndEndpoint(@Param("modelName") String modelName,
                                                              @Param("endpointType") Integer endpointType,
                                                              @Param("excludeIds") Set<Long> excludeIds);
}
