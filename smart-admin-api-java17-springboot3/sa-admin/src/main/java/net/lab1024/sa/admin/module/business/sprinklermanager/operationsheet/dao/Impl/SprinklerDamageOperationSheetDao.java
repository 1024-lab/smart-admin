package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.Impl.SprinklerDamageOperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerDamageOperationSheetQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo.SprinklerDamageOperationSheetVO;
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
public interface SprinklerDamageOperationSheetDao extends BaseDao<SprinklerDamageOperationSheetEntity> {

    /**
     * 分页查询喷头领用记录
     */
    List<SprinklerDamageOperationSheetVO> queryPageDamageOperationSheetList(Page<?> page, SprinklerDamageOperationSheetQueryForm queryForm);
}
