package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl;

import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.StockInOperationSheetEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface StockInOperationSheepDao extends BaseDao<StockInOperationSheetEntity> {
}
