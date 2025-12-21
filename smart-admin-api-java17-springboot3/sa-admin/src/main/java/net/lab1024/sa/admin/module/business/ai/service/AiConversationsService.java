package net.lab1024.sa.admin.module.business.ai.service;

import java.util.List;
import net.lab1024.sa.admin.module.business.ai.dao.AiConversationsDao;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiConversationsEntity;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiConversationsAddForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiConversationsQueryForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiConversationsUpdateForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiConversationsVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 会话表 Service
 *
 * @Author Mission
 * @Date 2025-12-21 16:08:28
 * @Copyright Mission
 */

@Service
public class AiConversationsService {

    @Resource
    private AiConversationsDao aiConversationsDao;

    /**
     * 分页查询
     */
    public PageResult<AiConversationsVO> queryPage(AiConversationsQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AiConversationsVO> list = aiConversationsDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }


    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<String> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        aiConversationsDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(String id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        aiConversationsDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
