package net.lab1024.sa.admin.module.business.knowledge.service;

import java.util.List;
import net.lab1024.sa.admin.module.business.knowledge.dao.AiKnowledgeDao;
import net.lab1024.sa.admin.module.business.knowledge.domain.entity.AiKnowledgeEntity;
import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiKnowledgeAddForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiKnowledgeQueryForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiKnowledgeUpdateForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.vo.AiKnowledgeVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 知识库 Service
 *
 * @Author Mission
 * @Date 2026-01-28 10:24:07
 * @Copyright Mission
 */

@Service
public class AiKnowledgeService {

    @Resource
    private AiKnowledgeDao aiKnowledgeDao;

    /**
     * 分页查询
     */
    public PageResult<AiKnowledgeVO> queryPage(AiKnowledgeQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AiKnowledgeVO> list = aiKnowledgeDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(AiKnowledgeAddForm addForm) {
        AiKnowledgeEntity aiKnowledgeEntity = SmartBeanUtil.copy(addForm, AiKnowledgeEntity.class);
        aiKnowledgeDao.insert(aiKnowledgeEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(AiKnowledgeUpdateForm updateForm) {
        AiKnowledgeEntity aiKnowledgeEntity = SmartBeanUtil.copy(updateForm, AiKnowledgeEntity.class);
        aiKnowledgeDao.updateById(aiKnowledgeEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<String> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        aiKnowledgeDao.batchUpdateDeleted(idList, true);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(String id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        aiKnowledgeDao.updateDeleted(id, true);
        return ResponseDTO.ok();
    }
}
