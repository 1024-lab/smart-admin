package net.lab1024.sa.admin.module.business.knowledge.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.knowledge.domain.entity.AiDocumentsEntity;
import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiDocumentsQueryForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.vo.AiDocumentsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 文档列表 Dao
 *
 * @Author Mission
 * @Date 2026-01-28 20:36:21
 * @Copyright Mission
 */

@Mapper
public interface AiDocumentsDao extends BaseMapper<AiDocumentsEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<AiDocumentsVO> queryPage(Page page, @Param("queryForm") AiDocumentsQueryForm queryForm);

}
