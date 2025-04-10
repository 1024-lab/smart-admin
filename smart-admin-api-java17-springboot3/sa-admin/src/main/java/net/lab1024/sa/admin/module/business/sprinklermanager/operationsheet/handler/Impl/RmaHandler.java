package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.Impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl.SprinklerRmaOperationSheetDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerRmaOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerRmaOperationSheetCreateForm;
import org.springframework.stereotype.Component;

@Component
public class RmaHandler extends BaseSprinklerHandler<SprinklerRmaOperationSheetCreateForm, SprinklerRmaOperationSheetEntity>{

    @Override
    protected Byte getSprinklerStatus() {
        return Byte.valueOf("4");
    }

    @Resource
    private SprinklerRmaOperationSheetDao sprinklerRmaOperationSheetDao;


    @Override
    public String getOperationType() {
        return "rma";
    }

    @Override
    protected Boolean validateBusiness(SprinklerRmaOperationSheetCreateForm form) {
        return Boolean.FALSE;
    }

    @Override
    protected BaseDao<SprinklerRmaOperationSheetEntity> getDao() {
        return sprinklerRmaOperationSheetDao;
    }

    @Override
    protected Class<SprinklerRmaOperationSheetEntity> getEntityClass() {
        return SprinklerRmaOperationSheetEntity.class;
    }

    @Override
    protected String getBizType() {
        return "喷头rma记录表";
    }
}
