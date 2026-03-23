package net.lab1024.sa.admin.module.business.ai.relay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayUsageLogEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayUsageLogVO;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayUsageLogQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RelayUsageLogDao extends BaseMapper<RelayUsageLogEntity> {

    List<RelayUsageLogVO> queryPage(Page page, @Param("queryForm") RelayUsageLogQueryForm queryForm);
}
