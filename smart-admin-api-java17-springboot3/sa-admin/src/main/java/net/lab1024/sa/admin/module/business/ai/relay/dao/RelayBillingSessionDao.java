package net.lab1024.sa.admin.module.business.ai.relay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayBillingSessionEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayBillingSessionVO;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayBillingSessionQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RelayBillingSessionDao extends BaseMapper<RelayBillingSessionEntity> {

    List<RelayBillingSessionVO> queryPage(Page page, @Param("queryForm") RelayBillingSessionQueryForm queryForm);
}
