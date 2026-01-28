package net.lab1024.sa.admin.module.business.knowledge.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.knowledge.domain.entity.AiDocumentSegmentsEntity;
import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiDocumentSegmentsQueryForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.vo.AiDocumentSegmentsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 文档分块表 Dao
 *
 * @Author Mission
 * @Date 2026-01-28 20:45:56
 * @Copyright Mission
 */

@Mapper
public interface AiDocumentSegmentsDao extends BaseMapper<AiDocumentSegmentsEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<AiDocumentSegmentsVO> queryPage(Page page, @Param("queryForm") AiDocumentSegmentsQueryForm queryForm);

}
