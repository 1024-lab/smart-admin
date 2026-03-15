package net.lab1024.sa.admin.module.business.ai.relay.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.constant.RelayErrorCode;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayModelDao;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayModelEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayModelAddForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayModelQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayModelUpdateForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayModelVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class RelayModelAdminService {

    @Resource
    private RelayModelDao relayModelDao;

    public PageResult<RelayModelVO> queryPage(RelayModelQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<RelayModelVO> list = relayModelDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    public ResponseDTO<String> add(RelayModelAddForm addForm) {
        RelayModelEntity exist = relayModelDao.selectByModelName(addForm.getModelName());
        if (exist != null) {
            return ResponseDTO.error(RelayErrorCode.MODEL_NAME_DUPLICATE);
        }
        RelayModelEntity entity = SmartBeanUtil.copy(addForm, RelayModelEntity.class);
        entity.setDisabledFlag(0);
        entity.setDeletedFlag(0);
        relayModelDao.insert(entity);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> update(RelayModelUpdateForm updateForm) {
        RelayModelEntity entity = relayModelDao.selectById(updateForm.getModelId());
        if (entity == null || entity.getDeletedFlag() == 1) {
            return ResponseDTO.error(RelayErrorCode.MODEL_NOT_FOUND);
        }
        RelayModelEntity byName = relayModelDao.selectByModelName(updateForm.getModelName());
        if (byName != null && !byName.getModelId().equals(updateForm.getModelId())) {
            return ResponseDTO.error(RelayErrorCode.MODEL_NAME_DUPLICATE);
        }
        entity.setModelName(updateForm.getModelName());
        entity.setEndpointType(updateForm.getEndpointType());
        entity.setInputPricePerK(updateForm.getInputPricePerK());
        entity.setOutputPricePerK(updateForm.getOutputPricePerK());
        entity.setRemark(updateForm.getRemark());
        relayModelDao.updateById(entity);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> delete(Long modelId) {
        RelayModelEntity entity = relayModelDao.selectById(modelId);
        if (entity == null) {
            return ResponseDTO.ok();
        }
        entity.setDeletedFlag(1);
        relayModelDao.updateById(entity);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> updateDisabledFlag(Long modelId, Integer disabledFlag) {
        RelayModelEntity entity = relayModelDao.selectById(modelId);
        if (entity == null || entity.getDeletedFlag() == 1) {
            return ResponseDTO.error(RelayErrorCode.MODEL_NOT_FOUND);
        }
        entity.setDisabledFlag(disabledFlag);
        relayModelDao.updateById(entity);
        return ResponseDTO.ok();
    }
}
