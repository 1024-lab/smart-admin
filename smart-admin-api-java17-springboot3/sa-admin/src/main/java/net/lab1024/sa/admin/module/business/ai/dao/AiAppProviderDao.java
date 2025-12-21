package net.lab1024.sa.admin.module.business.ai.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiAppProviderEntity;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppProviderQueryForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiAppProviderVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 应用提供商 Dao
 *
 * @Author Mission
 * @Date 2025-12-21 16:06:19
 * @Copyright Mission
 */

@Mapper
public interface AiAppProviderDao extends BaseMapper<AiAppProviderEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<AiAppProviderVO> queryPage(Page page, @Param("queryForm") AiAppProviderQueryForm queryForm);

}
