package net.lab1024.sa.admin.module.business.ai.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiAppEntity;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppQueryForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiAppVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * ai应用管理表 Dao
 *
 * @Author Mission
 * @Date 2025-12-21 16:00:44
 * @Copyright Mission
 */

@Mapper
public interface AiAppDao extends BaseMapper<AiAppEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<AiAppVO> queryPage(Page page, @Param("queryForm") AiAppQueryForm queryForm);

}
