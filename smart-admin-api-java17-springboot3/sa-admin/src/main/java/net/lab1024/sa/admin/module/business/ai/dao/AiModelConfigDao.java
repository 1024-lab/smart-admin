package net.lab1024.sa.admin.module.business.ai.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiModelConfigEntity;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiModelConfigQueryForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiModelConfigVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 模型配置表 Dao
 *
 * @Author Mission
 * @Date 2026-01-28 09:59:38
 * @Copyright Mission
 */

@Mapper
public interface AiModelConfigDao extends BaseMapper<AiModelConfigEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<AiModelConfigVO> queryPage(Page page, @Param("queryForm") AiModelConfigQueryForm queryForm);

    /**
     * 更新删除状态
     */
    long updateDeleted(@Param("id")Integer id,@Param("deletedFlag")boolean deletedFlag);

    /**
     * 批量更新删除状态
     */
    void batchUpdateDeleted(@Param("idList")List<Integer> idList,@Param("deletedFlag")boolean deletedFlag);

}
