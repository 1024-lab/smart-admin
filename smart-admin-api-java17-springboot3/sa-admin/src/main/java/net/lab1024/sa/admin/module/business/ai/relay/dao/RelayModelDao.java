package net.lab1024.sa.admin.module.business.ai.relay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayModelEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayModelVO;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayModelQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RelayModelDao extends BaseMapper<RelayModelEntity> {

    List<RelayModelVO> queryPage(Page page, @Param("queryForm") RelayModelQueryForm queryForm);

    RelayModelEntity selectByModelName(@Param("modelName") String modelName);

    List<RelayModelEntity> selectAllEnabled();
}
