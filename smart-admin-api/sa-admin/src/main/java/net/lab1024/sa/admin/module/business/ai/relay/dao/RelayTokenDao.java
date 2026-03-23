package net.lab1024.sa.admin.module.business.ai.relay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayTokenEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayTokenVO;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayTokenQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RelayTokenDao extends BaseMapper<RelayTokenEntity> {

    List<RelayTokenVO> queryPage(Page page, @Param("queryForm") RelayTokenQueryForm queryForm);

    RelayTokenEntity selectByTokenHash(@Param("tokenHash") String tokenHash);
}
