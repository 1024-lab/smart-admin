package net.lab1024.sa.admin.module.business.ai.relay.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.ai.relay.constant.RelayErrorCode;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayChannelDao;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayChannelModelDao;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayChannelEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayChannelModelEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelAddForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelModelBindForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelUpdateForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayChannelVO;
import net.lab1024.sa.admin.module.business.ai.relay.service.crypto.ApiKeyCipher;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RelayChannelAdminService {

    @Resource
    private RelayChannelDao relayChannelDao;

    @Resource
    private RelayChannelModelDao relayChannelModelDao;

    @Resource
    private ApiKeyCipher apiKeyCipher;

    public PageResult<RelayChannelVO> queryPage(RelayChannelQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<RelayChannelVO> list = relayChannelDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    public List<RelayChannelVO> queryList(RelayChannelQueryForm queryForm) {
        return relayChannelDao.queryList(queryForm);
    }

    public ResponseDTO<String> add(RelayChannelAddForm addForm) {
        RelayChannelEntity entity = SmartBeanUtil.copy(addForm, RelayChannelEntity.class);
        String plainKey = addForm.getApiKey();
        entity.setApiKeyCipher(apiKeyCipher.encrypt(plainKey));
        entity.setApiKeyKid(apiKeyCipher.currentKid());
        entity.setApiKeyMask(apiKeyCipher.mask(plainKey));
        entity.setDisabledFlag(0);
        entity.setDeletedFlag(0);
        if (entity.getWeight() == null) {
            entity.setWeight(1);
        }
        if (entity.getTimeoutMs() == null) {
            entity.setTimeoutMs(60000);
        }
        if (entity.getMaxRetries() == null) {
            entity.setMaxRetries(1);
        }
        relayChannelDao.insert(entity);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> update(RelayChannelUpdateForm updateForm) {
        RelayChannelEntity entity = relayChannelDao.selectById(updateForm.getChannelId());
        if (entity == null || entity.getDeletedFlag() == 1) {
            return ResponseDTO.error(RelayErrorCode.CHANNEL_NOT_FOUND);
        }
        entity.setChannelName(updateForm.getChannelName());
        entity.setProviderType(updateForm.getProviderType());
        entity.setBaseUrl(updateForm.getBaseUrl());
        entity.setOrganization(updateForm.getOrganization());
        entity.setPriority(updateForm.getPriority());
        entity.setWeight(updateForm.getWeight());
        entity.setTimeoutMs(updateForm.getTimeoutMs());
        entity.setMaxRetries(updateForm.getMaxRetries());
        entity.setRemark(updateForm.getRemark());
        String newKey = updateForm.getApiKey();
        if (newKey != null && !newKey.isBlank()) {
            entity.setApiKeyCipher(apiKeyCipher.encrypt(newKey));
            entity.setApiKeyKid(apiKeyCipher.currentKid());
            entity.setApiKeyMask(apiKeyCipher.mask(newKey));
        }
        relayChannelDao.updateById(entity);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> delete(Long channelId) {
        RelayChannelEntity entity = relayChannelDao.selectById(channelId);
        if (entity == null) {
            return ResponseDTO.ok();
        }
        entity.setDeletedFlag(1);
        relayChannelDao.updateById(entity);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> updateDisabledFlag(Long channelId, Integer disabledFlag) {
        RelayChannelEntity entity = relayChannelDao.selectById(channelId);
        if (entity == null || entity.getDeletedFlag() == 1) {
            return ResponseDTO.error(RelayErrorCode.CHANNEL_NOT_FOUND);
        }
        entity.setDisabledFlag(disabledFlag);
        relayChannelDao.updateById(entity);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> bindModels(RelayChannelModelBindForm form) {
        relayChannelModelDao.deleteByChannelId(form.getChannelId());
        for (Long modelId : form.getModelIds()) {
            RelayChannelModelEntity channelModelEntity = new RelayChannelModelEntity();
            channelModelEntity.setChannelId(form.getChannelId());
            channelModelEntity.setModelId(modelId);
            channelModelEntity.setDisabledFlag(0);
            relayChannelModelDao.insert(channelModelEntity);
        }
        return ResponseDTO.ok();
    }

    public List<Long> getBoundModelIds(Long channelId) {
        List<RelayChannelModelEntity> list = relayChannelModelDao.selectByChannelId(channelId);
        return list.stream().map(RelayChannelModelEntity::getModelId).toList();
    }
}
