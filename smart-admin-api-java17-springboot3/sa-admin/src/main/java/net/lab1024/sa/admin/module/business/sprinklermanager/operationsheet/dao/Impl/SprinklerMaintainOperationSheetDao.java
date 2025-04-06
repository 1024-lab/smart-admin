package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerAllocateOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerMaintainOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerAllocateOperationSheetQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerMaintainOperationSheetQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo.SprinklerAllocateOperationSheetVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo.SprinklerMaintainOperationSheetVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 喷头领用记录
 *
 * @Author 海印:芦苇
 */
@Mapper
@Component
public interface SprinklerMaintainOperationSheetDao extends BaseDao<SprinklerMaintainOperationSheetEntity> {

    /**
     * 分页查询喷头维修记录
     */
    List<SprinklerMaintainOperationSheetVO> queryPageMaintainOperationSheetList(Page<?> page, SprinklerMaintainOperationSheetQueryForm queryForm);
}
