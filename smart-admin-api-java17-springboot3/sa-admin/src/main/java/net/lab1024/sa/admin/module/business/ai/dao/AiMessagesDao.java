package net.lab1024.sa.admin.module.business.ai.dao;

import java.util.List;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiMessagesEntity;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiMessagesQueryForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiMessagesVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 消息表 Dao
 *
 * @Author Mission
 * @Date 2025-12-21 16:10:16
 * @Copyright Mission
 */

@Mapper
public interface AiMessagesDao extends BaseMapper<AiMessagesEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<AiMessagesVO> queryPage(Page page, @Param("queryForm") AiMessagesQueryForm queryForm);

}
