package net.lab1024.sa.admin.module.business.ai.service;

import java.util.List;
import net.lab1024.sa.admin.module.business.ai.dao.AiAppDao;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiAppEntity;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppAddForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppQueryForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppUpdateForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiAppVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * ai应用管理表 Service
 *
 * @Author Mission
 * @Date 2025-12-21 16:00:44
 * @Copyright Mission
 */

@Service
public class AiAppService {

    @Resource
    public AiAppDao aiAppDao;

    /**
     * 分页查询
     */
    public PageResult<AiAppVO> queryPage(AiAppQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AiAppVO> list = aiAppDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(AiAppAddForm addForm) {
        AiAppEntity aiAppEntity = SmartBeanUtil.copy(addForm, AiAppEntity.class);
        aiAppDao.insert(aiAppEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(AiAppUpdateForm updateForm) {
        AiAppEntity aiAppEntity = SmartBeanUtil.copy(updateForm, AiAppEntity.class);
        aiAppDao.updateById(aiAppEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<String> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        aiAppDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(String id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        aiAppDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
