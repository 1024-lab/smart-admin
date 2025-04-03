package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.entity.EnterpriseEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.OperationSheetEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 喷头-操作表信息
 *
 * @Author 海印:芦苇
 */
@Mapper
@Component
public interface OperationSheetDao extends BaseMapper<OperationSheetEntity> {
}
