package net.lab1024.sa.admin.module.business.ai.relay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayQuotaLedgerEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayQuotaLedgerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RelayQuotaLedgerDao extends BaseMapper<RelayQuotaLedgerEntity> {

    List<RelayQuotaLedgerVO> queryPageByEmployeeId(Page page, @Param("employeeId") Long employeeId);
}
