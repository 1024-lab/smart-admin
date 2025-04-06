package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.validation.constraints.NotNull;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.OperationSheetEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * 喷头操作记录
 *
 * @Author 海印:芦苇
 */
@Mapper
@Component
public interface OperationSheetDao extends BaseMapper<OperationSheetEntity> {
    @Select("SELECT operation_sheet_id FROM t_operation_sheet WHERE sprinkler_id = #{sprinklerId}")
    @NotNull(message = "喷头Id不能为空") Long findBySprinklerId(Long sprinklerId);
}
