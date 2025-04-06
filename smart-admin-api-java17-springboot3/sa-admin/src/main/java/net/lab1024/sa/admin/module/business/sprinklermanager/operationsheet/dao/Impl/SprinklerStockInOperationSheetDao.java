package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerStockInOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerStockInOperationSheetQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo.SprinklerStockInOperationSheetVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 喷头入库记录
 *
 * @Author 海印:芦苇
 */
@Mapper
@Component
public interface SprinklerStockInOperationSheetDao extends BaseDao<SprinklerStockInOperationSheetEntity> {

    /**
     * 分页查询喷头入库记录
     */
    List<SprinklerStockInOperationSheetVO> queryPageStockInOperationSheetList(Page<?> page, SprinklerStockInOperationSheetQueryForm queryForm);
}
