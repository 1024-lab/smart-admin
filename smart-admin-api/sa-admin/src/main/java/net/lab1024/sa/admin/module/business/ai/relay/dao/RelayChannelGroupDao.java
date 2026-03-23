package net.lab1024.sa.admin.module.business.ai.relay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayChannelGroupEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelGroupQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayChannelGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RelayChannelGroupDao extends BaseMapper<RelayChannelGroupEntity> {

    List<RelayChannelGroupVO> queryPage(Page page, @Param("queryForm") RelayChannelGroupQueryForm queryForm);

    List<RelayChannelGroupVO> queryList(@Param("queryForm") RelayChannelGroupQueryForm queryForm);
}
