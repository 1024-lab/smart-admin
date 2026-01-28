package net.lab1024.sa.admin.module.business.ai.service;

import java.util.List;
import net.lab1024.sa.admin.module.business.ai.dao.AiModelConfigDao;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiModelConfigEntity;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiModelConfigAddForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiModelConfigQueryForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiModelConfigUpdateForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiModelConfigVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 模型配置表 Service
 *
 * @Author Mission
 * @Date 2026-01-28 09:59:38
 * @Copyright Mission
 */

@Service
public class AiModelConfigService {

    @Resource
    private AiModelConfigDao aiModelConfigDao;

    /**
     * 分页查询
     */
    public PageResult<AiModelConfigVO> queryPage(AiModelConfigQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AiModelConfigVO> list = aiModelConfigDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(AiModelConfigAddForm addForm) {
        AiModelConfigEntity aiModelConfigEntity = SmartBeanUtil.copy(addForm, AiModelConfigEntity.class);
        aiModelConfigDao.insert(aiModelConfigEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(AiModelConfigUpdateForm updateForm) {
        AiModelConfigEntity aiModelConfigEntity = SmartBeanUtil.copy(updateForm, AiModelConfigEntity.class);
        aiModelConfigDao.updateById(aiModelConfigEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        aiModelConfigDao.batchUpdateDeleted(idList, true);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Integer id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        aiModelConfigDao.updateDeleted(id, true);
        return ResponseDTO.ok();
    }
}
