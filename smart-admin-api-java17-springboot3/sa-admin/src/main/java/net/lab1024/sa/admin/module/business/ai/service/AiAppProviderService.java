package net.lab1024.sa.admin.module.business.ai.service;

import java.util.List;
import net.lab1024.sa.admin.module.business.ai.dao.AiAppProviderDao;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiAppProviderEntity;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppProviderAddForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppProviderQueryForm;
import net.lab1024.sa.admin.module.business.ai.domain.form.AiAppProviderUpdateForm;
import net.lab1024.sa.admin.module.business.ai.domain.vo.AiAppProviderVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 应用提供商 Service
 *
 * @Author Mission
 * @Date 2025-12-21 16:06:19
 * @Copyright Mission
 */

@Service
public class AiAppProviderService {

    @Resource
    public AiAppProviderDao aiAppProviderDao;

    /**
     * 分页查询
     */
    public PageResult<AiAppProviderVO> queryPage(AiAppProviderQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AiAppProviderVO> list = aiAppProviderDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(AiAppProviderAddForm addForm) {
        AiAppProviderEntity aiAppProviderEntity = SmartBeanUtil.copy(addForm, AiAppProviderEntity.class);
        aiAppProviderDao.insert(aiAppProviderEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(AiAppProviderUpdateForm updateForm) {
        AiAppProviderEntity aiAppProviderEntity = SmartBeanUtil.copy(updateForm, AiAppProviderEntity.class);
        aiAppProviderDao.updateById(aiAppProviderEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<String> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        aiAppProviderDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(String id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        aiAppProviderDao.deleteById(id);
        return ResponseDTO.ok();
    }

    /**
     * 查询所有供应商
     */
    public List<AiAppProviderVO> queryAll() {
        List<AiAppProviderEntity> list = aiAppProviderDao.selectList(null);
        return SmartBeanUtil.copyList(list, AiAppProviderVO.class);
    }
}
