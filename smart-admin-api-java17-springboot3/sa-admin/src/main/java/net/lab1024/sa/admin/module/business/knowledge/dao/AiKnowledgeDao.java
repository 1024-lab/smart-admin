package net.lab1024.sa.admin.module.business.knowledge.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.knowledge.domain.entity.AiKnowledgeEntity;
import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiKnowledgeQueryForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.vo.AiKnowledgeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 知识库 Dao
 *
 * @Author Mission
 * @Date 2026-01-28 10:24:07
 * @Copyright Mission
 */

@Mapper
public interface AiKnowledgeDao extends BaseMapper<AiKnowledgeEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<AiKnowledgeVO> queryPage(Page page, @Param("queryForm") AiKnowledgeQueryForm queryForm);

    /**
     * 更新删除状态
     */
    long updateDeleted(@Param("id")String id,@Param("deletedFlag")boolean deletedFlag);

    /**
     * 批量更新删除状态
     */
    void batchUpdateDeleted(@Param("idList")List<String> idList,@Param("deletedFlag")boolean deletedFlag);

}
