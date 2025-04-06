package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.Impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl.SprinklerAllocateOperationSheetDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerAllocateOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerAllocateOperationSheetCreateForm;
import org.springframework.stereotype.Component;

@Component
public class AllocateHandler extends BaseSprinklerHandler<SprinklerAllocateOperationSheetCreateForm, SprinklerAllocateOperationSheetEntity>{

    @Resource
    private SprinklerAllocateOperationSheetDao sprinklerAllocateOperationSheetDao;


    @Override
    public String getOperationType() {
        return "allocate";
    }

    @Override
    protected Boolean validateBusiness(SprinklerAllocateOperationSheetCreateForm form) {
        return Boolean.FALSE;
    }

    @Override
    protected BaseDao<SprinklerAllocateOperationSheetEntity> getDao() {
        return sprinklerAllocateOperationSheetDao;
    }

    @Override
    protected Class<SprinklerAllocateOperationSheetEntity> getEntityClass() {
        return SprinklerAllocateOperationSheetEntity.class;
    }

    @Override
    protected String getBizType() {
        return "喷头领用记录表";
    }
}
