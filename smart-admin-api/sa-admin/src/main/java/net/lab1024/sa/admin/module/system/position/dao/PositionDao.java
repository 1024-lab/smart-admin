package net.lab1024.sa.admin.module.system.position.dao;

import java.util.List;
import net.lab1024.sa.admin.module.system.position.domain.entity.PositionEntity;
import net.lab1024.sa.admin.module.system.position.domain.form.PositionQueryForm;
import net.lab1024.sa.admin.module.system.position.domain.vo.PositionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 职务表 Dao
 *
 * @Author kaiyun
 * @Date 2024-06-23 23:31:38
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */

@Mapper
@Component
public interface PositionDao extends BaseMapper<PositionEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<PositionVO> queryPage(Page page, @Param("queryForm") PositionQueryForm queryForm);


    /**
     * 查询
     * @param deletedFlag
     * @return
     */
    List<PositionVO> queryList(@Param("deletedFlag") Boolean deletedFlag);
}
