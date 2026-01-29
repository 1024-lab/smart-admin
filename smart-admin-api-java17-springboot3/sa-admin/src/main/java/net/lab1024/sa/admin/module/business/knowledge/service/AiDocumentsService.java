package net.lab1024.sa.admin.module.business.knowledge.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import cn.dev33.satoken.stp.StpUtil;
import net.lab1024.sa.admin.module.business.knowledge.dao.AiDocumentsDao;
import net.lab1024.sa.admin.module.business.knowledge.domain.entity.AiDocumentsEntity;
import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiDocumentsAddForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.form.AiDocumentsQueryForm;
import net.lab1024.sa.admin.module.business.knowledge.domain.vo.AiDocumentsVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
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
     * 上传文档
     */
    public ResponseDTO<String> upload(AiDocumentsAddForm addForm) {
        AiDocumentsEntity entity = SmartBeanUtil.copy(addForm, AiDocumentsEntity.class);
        entity.setId(UUID.randomUUID().toString());
        entity.setStatus("pending");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        // 设置创建人信息
        if (StpUtil.isLogin()) {
            String userId = StpUtil.getLoginIdAsString();
            entity.setCreateBy(userId);
            entity.setUpdateBy(userId);
        }

        aiDocumentsDao.insert(entity);
        return ResponseDTO.ok(entity.getId());
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
