package net.lab1024.sa.admin.module.business.ai.relay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayUserAccountEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayUserAccountVO;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayUserAccountQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface RelayUserAccountDao extends BaseMapper<RelayUserAccountEntity> {

    List<RelayUserAccountVO> queryPage(Page page, @Param("queryForm") RelayUserAccountQueryForm queryForm);

    int preconsumeQuota(@Param("employeeId") Long employeeId, @Param("amount") BigDecimal amount);

    int restoreQuota(@Param("employeeId") Long employeeId, @Param("amount") BigDecimal amount);

    int adjustQuota(@Param("employeeId") Long employeeId, @Param("delta") BigDecimal delta);
}
