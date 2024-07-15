package net.lab1024.sa.base.module.support.job.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.base.module.support.job.api.domain.SmartJobQueryForm;
import net.lab1024.sa.base.module.support.job.api.domain.SmartJobVO;
import net.lab1024.sa.base.module.support.job.repository.domain.SmartJobEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务 dao
 *
 * @author huke
 * @date 2024/6/17 21:30
 */
@Mapper
@Component
public interface SmartJobDao extends BaseMapper<SmartJobEntity> {

    /**
     * 定时任务-分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<SmartJobVO> query(Page<?> page, @Param("query") SmartJobQueryForm queryForm);
}
