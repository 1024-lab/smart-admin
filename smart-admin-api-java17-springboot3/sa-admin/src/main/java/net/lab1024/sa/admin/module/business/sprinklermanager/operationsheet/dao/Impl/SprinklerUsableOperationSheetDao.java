package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerUsableOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerUsableOperationSheetQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo.SprinklerUsableOperationSheetVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 喷头usable记录
 *
 * @Author 海印:芦苇
 */
@Mapper
@Component
public interface SprinklerUsableOperationSheetDao extends BaseDao<SprinklerUsableOperationSheetEntity> {

    /**
     * 分页查询喷头维修记录
     */
    List<SprinklerUsableOperationSheetVO> queryPageUsableOperationSheetList(Page<?> page, SprinklerUsableOperationSheetQueryForm queryForm);
}
