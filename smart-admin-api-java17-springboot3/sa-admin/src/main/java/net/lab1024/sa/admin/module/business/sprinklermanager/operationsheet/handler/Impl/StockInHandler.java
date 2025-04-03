package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.Impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl.StockInOperationSheepDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.StockInOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.StockInOperationSheetCreateForm;
import org.springframework.stereotype.Component;

@Component
public class StockInHandler extends BaseSprinklerHandler<StockInOperationSheetCreateForm, StockInOperationSheetEntity>{



    @Override
    public String getOperationType() {
        return "stock_in";
    }

    @Override
    protected Boolean validateBusiness(StockInOperationSheetCreateForm form) {
        return Boolean.FALSE;
    }

    @Resource
    private StockInOperationSheepDao stockInOperationSheepDao;

    @Override
    protected BaseDao<StockInOperationSheetEntity> getDao() {
        return stockInOperationSheepDao;
    }

    @Override
    protected Class<StockInOperationSheetEntity> getEntityClass() {
        return StockInOperationSheetEntity.class;
    }

    @Override
    protected String getBizType() {
        return "入库喷头记录表";
    }
}
