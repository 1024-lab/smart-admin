package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.Impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl.SprinklerMaintainOperationSheetDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerMaintainOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerMaintainOperationSheetCreateForm;
import org.springframework.stereotype.Component;

@Component
public class MaintainHandler extends BaseSprinklerHandler<SprinklerMaintainOperationSheetCreateForm, SprinklerMaintainOperationSheetEntity>{

    @Override
    protected Byte getSprinklerStatus() {
        return Byte.valueOf("2");
    }

    @Resource
    private SprinklerMaintainOperationSheetDao sprinklerMaintainOperationSheetDao;


    @Override
    public String getOperationType() {
        return "maintain";
    }

    @Override
    protected Boolean validateBusiness(SprinklerMaintainOperationSheetCreateForm form) {
        return Boolean.FALSE;
    }

    @Override
    protected BaseDao<SprinklerMaintainOperationSheetEntity> getDao() {
        return sprinklerMaintainOperationSheetDao;
    }

    @Override
    protected Class<SprinklerMaintainOperationSheetEntity> getEntityClass() {
        return SprinklerMaintainOperationSheetEntity.class;
    }

    @Override
    protected String getBizType() {
        return "喷头维修记录表";
    }
}
