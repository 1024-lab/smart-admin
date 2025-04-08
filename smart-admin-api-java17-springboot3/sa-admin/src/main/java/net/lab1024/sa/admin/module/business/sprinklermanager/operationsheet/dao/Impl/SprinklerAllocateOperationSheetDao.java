package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerAllocateOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerAllocateOperationSheetQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo.SprinklerAllocateOperationSheetVO;
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
public interface SprinklerAllocateOperationSheetDao extends BaseDao<SprinklerAllocateOperationSheetEntity> {

    /**
     * 分页查询喷头领用记录
     */
    List<SprinklerAllocateOperationSheetVO> queryPageAllocateOperationSheetList(Page<?> page, SprinklerAllocateOperationSheetQueryForm queryForm);
}
