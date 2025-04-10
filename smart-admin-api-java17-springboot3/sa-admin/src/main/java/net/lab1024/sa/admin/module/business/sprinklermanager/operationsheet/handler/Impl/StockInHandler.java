package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.Impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl.SprinklerStockInOperationSheetDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerStockInOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerStockInOperationSheetCreateForm;
import org.springframework.stereotype.Component;

@Component
public class StockInHandler extends BaseSprinklerHandler<SprinklerStockInOperationSheetCreateForm, SprinklerStockInOperationSheetEntity>{

    @Override
    protected Byte getSprinklerStatus() {
        return Byte.valueOf("0");
    }



    @Override
    public String getOperationType() {
        return "stock_in";
    }

    @Override
    protected Boolean validateBusiness(SprinklerStockInOperationSheetCreateForm form) {
        return Boolean.FALSE;
    }

    @Resource
    private SprinklerStockInOperationSheetDao stockInOperationSheepDao;

    @Override
    protected BaseDao<SprinklerStockInOperationSheetEntity> getDao() {
        return stockInOperationSheepDao;
    }

    @Override
    protected Class<SprinklerStockInOperationSheetEntity> getEntityClass() {
        return SprinklerStockInOperationSheetEntity.class;
    }

    @Override
    protected String getBizType() {
        return "喷头入库记录表";
    }
}
