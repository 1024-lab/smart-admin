package net.lab1024.sa.common.module.support.datatracer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.common.module.support.datatracer.domain.entity.DataTracerEntity;
import net.lab1024.sa.common.module.support.datatracer.domain.form.DataTracerQueryForm;
import net.lab1024.sa.common.module.support.datatracer.domain.vo.DataTracerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * dao： t_data_tracker
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-07-23 19:38:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Mapper
@Component
public interface DataTracerDao extends BaseMapper<DataTracerEntity> {

    /**
     * 操作记录查询
     *
     * @return
     */
    List<DataTracerVO> selectRecord(@Param("dataId") Long dataId, @Param("dataType") Integer dataType);

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<DataTracerVO> query(Page page, @Param("query") DataTracerQueryForm queryForm);
}
