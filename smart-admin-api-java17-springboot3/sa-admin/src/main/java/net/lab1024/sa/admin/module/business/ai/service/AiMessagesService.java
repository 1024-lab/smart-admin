package net.lab1024.sa.admin.module.business.ai.service;

import java.util.List;
import net.lab1024.sa.admin.module.business.ai.dao.AiMessagesDao;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiMessagesEntity;
//import net.lab1024.sa.admin.module.business.ai.domain.form.AiMessagesAddForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiMessagesQueryForm;
//import net.lab1024.sa.admin.module.business.ai.domain.form.AiMessagesUpdateForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiMessagesVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 消息表 Service
 *
 * @Author Mission
 * @Date 2025-12-21 16:10:16
 * @Copyright Mission
 */

@Service
public class AiMessagesService {

    @Resource
    private AiMessagesDao aiMessagesDao;

    /**
     * 分页查询
     */
    public PageResult<AiMessagesVO> queryPage(AiMessagesQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AiMessagesVO> list = aiMessagesDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }


    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<String> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        aiMessagesDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(String id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        aiMessagesDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
