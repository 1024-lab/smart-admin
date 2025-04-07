package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.Impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl.SprinklerUsableOperationSheetDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerUsableOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerUsableOperationSheetCreateForm;
import org.springframework.stereotype.Component;

@Component
public class UsableHandler extends BaseSprinklerHandler<SprinklerUsableOperationSheetCreateForm, SprinklerUsableOperationSheetEntity>{

    @Resource
    private SprinklerUsableOperationSheetDao sprinklerUsableOperationSheetDao;


    @Override
    public String getOperationType() {
        return "usable";
    }

    @Override
    protected Boolean validateBusiness(SprinklerUsableOperationSheetCreateForm form) {
        return Boolean.FALSE;
    }

    @Override
    protected BaseDao<SprinklerUsableOperationSheetEntity> getDao() {
        return sprinklerUsableOperationSheetDao;
    }

    @Override
    protected Class<SprinklerUsableOperationSheetEntity> getEntityClass() {
        return SprinklerUsableOperationSheetEntity.class;
    }

    @Override
    protected String getBizType() {
        return "可用旧喷头记录表";
    }
}
