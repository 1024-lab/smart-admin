package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotBlank;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerStockInEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerStockInQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerStockInExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerStockInVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 喷头
 *
 * @Author 海印: 芦苇
 */
@Mapper
@Component
public interface SprinklerStockInDao extends BaseMapper<SprinklerStockInEntity> {
    /**
     * 喷头分页查询
     *
     */
    List<SprinklerStockInVO> queryPage(Page page, @Param("queryForm") SprinklerStockInQueryForm queryForm);

    /**
     * 根据喷头序列号查询
     *
     */
    SprinklerStockInEntity queryBySprinklerSerial(@Param("sprinklerSerial") String sprinklerSerial, @Param("excludeSprinklerId") Long excludeSprinklerId, @Param("deletedFlag") Boolean deletedFlag);

    @Select("SELECT sprinkler_id FROM t_sprinkler WHERE sprinkler_serial = #{sprinklerSerial}")
    long findIdBySprinklerSerial(@NotBlank(message = "喷头序列号不能为空") @Length(max = 20, message = "sprinklerSerial最多20字符") String sprinklerSerial);

    SprinklerStockInVO getDetail(@Param("sprinklerId") Long sprinklerId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 删除喷头
     */
    void deleteSprinkler(@Param("sprinklerId") Long sprinklerId,@Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询导出的数据
     *
     */
    List<SprinklerStockInExcelVO> selectExcelExportData(@Param("queryForm")  SprinklerStockInQueryForm queryForm);
}
