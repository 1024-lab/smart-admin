package net.lab1024.sa.admin.module.business.sprinkler.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinkler.domain.vo.SprinklerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

}
