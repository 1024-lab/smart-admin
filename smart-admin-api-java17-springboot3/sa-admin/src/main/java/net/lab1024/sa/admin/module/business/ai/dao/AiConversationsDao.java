package net.lab1024.sa.admin.module.business.ai.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiConversationsEntity;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiConversationsQueryForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiConversationsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 会话表 Dao
 *
 * @Author Mission
 * @Date 2025-12-21 16:08:28
 * @Copyright Mission
 */

@Mapper
public interface AiConversationsDao extends BaseMapper<AiConversationsEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<AiConversationsVO> queryPage(Page page, @Param("queryForm") AiConversationsQueryForm queryForm);

}
