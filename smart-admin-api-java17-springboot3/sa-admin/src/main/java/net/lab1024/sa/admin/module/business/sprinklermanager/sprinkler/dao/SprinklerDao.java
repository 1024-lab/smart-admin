package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotBlank;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
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
public interface SprinklerDao extends BaseMapper<SprinklerEntity> {
    /**
     * 喷头分页查询
     *
     */
    List<SprinklerVO> queryPage(Page page, @Param("queryForm") SprinklerQueryForm queryForm);

    /**
     * 根据喷头序列号查询
     *
     */
    SprinklerEntity queryBySprinklerSerial(@Param("sprinklerSerial") String sprinklerSerial, @Param("excludeSprinklerId") Long excludeSprinklerId, @Param("deletedFlag") Boolean deletedFlag);

    @Select("SELECT sprinkler_id FROM t_sprinkler WHERE sprinkler_serial = #{sprinklerSerial}")
    long findIdBySprinklerSerial(@NotBlank(message = "喷头序列号不能为空") @Length(max = 20, message = "sprinklerSerial最多20字符") String sprinklerSerial);
}
