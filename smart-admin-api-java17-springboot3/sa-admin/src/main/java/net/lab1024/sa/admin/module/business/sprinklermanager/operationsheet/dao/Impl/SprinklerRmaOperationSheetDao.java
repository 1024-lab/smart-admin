package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerRmaOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerRmaOperationSheetQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo.SprinklerRmaOperationSheetVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 喷头Rma记录
 *
 * @Author 海印:芦苇
 */
@Mapper
@Component
public interface SprinklerRmaOperationSheetDao extends BaseDao<SprinklerRmaOperationSheetEntity> {

    /**
     * 分页查询喷头维修记录
     */
    List<SprinklerRmaOperationSheetVO> queryPageRmaOperationSheetList(Page<?> page, SprinklerRmaOperationSheetQueryForm queryForm);
}
