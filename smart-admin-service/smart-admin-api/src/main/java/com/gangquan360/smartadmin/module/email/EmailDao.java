package com.gangquan360.smartadmin.module.email;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.gangquan360.smartadmin.module.email.domain.dto.EmailQueryDTO;
import com.gangquan360.smartadmin.module.email.domain.entity.EmailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019-05-13 17:10:16
 * @since JDK1.8
 */
@Mapper
@Component
public interface EmailDao extends BaseMapper<EmailEntity> {

    /**
     * 分页查询
     * @param queryDTO
     * @return EmailEntity
    */
    List<EmailEntity> queryByPage(Pagination page, @Param("queryDTO") EmailQueryDTO queryDTO);

    /**
     * 根据id删除
     * @param id
     * @return
    */
    void deleteById(@Param("id") Long id);

    /**
     * 批量删除
     * @param idList
     * @return
    */
    void deleteByIds(@Param("idList") List<Long> idList);
}
