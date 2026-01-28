package net.lab1024.sa.admin.module.business.knowledge.service;

import java.util.List;
import net.lab1024.sa.admin.module.business.knowledge.dao.AiDocumentsDao;
import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiDocumentsQueryForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.vo.AiDocumentsVO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 文档列表 Service
 *
 * @Author Mission
 * @Date 2026-01-28 20:36:21
 * @Copyright Mission
 */

@Service
public class AiDocumentsService {

    @Resource
    private AiDocumentsDao aiDocumentsDao;

    /**
     * 分页查询
     */
    public PageResult<AiDocumentsVO> queryPage(AiDocumentsQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AiDocumentsVO> list = aiDocumentsDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }


    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<String> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        aiDocumentsDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(String id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        aiDocumentsDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
