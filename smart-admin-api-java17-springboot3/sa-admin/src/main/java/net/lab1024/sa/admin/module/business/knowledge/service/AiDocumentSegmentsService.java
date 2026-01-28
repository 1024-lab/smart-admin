package net.lab1024.sa.admin.module.business.knowledge.service;

import java.util.List;
import net.lab1024.sa.admin.module.business.knowledge.dao.AiDocumentSegmentsDao;
import net.lab1024.sa.admin.module.business.knowledge.domain.entity.AiDocumentSegmentsEntity;
import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiDocumentSegmentsQueryForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.vo.AiDocumentSegmentsVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 文档分块表 Service
 *
 * @Author Mission
 * @Date 2026-01-28 20:45:56
 * @Copyright Mission
 */

@Service
public class AiDocumentSegmentsService {

    @Resource
    private AiDocumentSegmentsDao aiDocumentSegmentsDao;

    /**
     * 分页查询
     */
    public PageResult<AiDocumentSegmentsVO> queryPage(AiDocumentSegmentsQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AiDocumentSegmentsVO> list = aiDocumentSegmentsDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }


    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        aiDocumentSegmentsDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        aiDocumentSegmentsDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
