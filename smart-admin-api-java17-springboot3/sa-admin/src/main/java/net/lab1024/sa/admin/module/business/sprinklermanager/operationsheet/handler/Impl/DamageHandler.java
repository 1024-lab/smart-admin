package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.Impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl.SprinklerDamageOperationSheetDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerDamageOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerDamageOperationSheetCreateForm;
import org.springframework.stereotype.Component;

@Component
public class DamageHandler extends BaseSprinklerHandler<SprinklerDamageOperationSheetCreateForm, SprinklerDamageOperationSheetEntity>{

    @Override
    protected Byte getSprinklerStatus() {
        return Byte.valueOf("3");
    }

    @Resource
    private SprinklerDamageOperationSheetDao sprinklerDamageOperationSheetDao;


    @Override
    public String getOperationType() {
        return "damage";
    }

    @Override
    protected Boolean validateBusiness(SprinklerDamageOperationSheetCreateForm form) {
        return Boolean.FALSE;
    }

    @Override
    protected BaseDao<SprinklerDamageOperationSheetEntity> getDao() {
        return sprinklerDamageOperationSheetDao;
    }

    @Override
    protected Class<SprinklerDamageOperationSheetEntity> getEntityClass() {
        return SprinklerDamageOperationSheetEntity.class;
    }

    @Override
    protected String getBizType() {
        return "喷头破损记录表";
    }
}
