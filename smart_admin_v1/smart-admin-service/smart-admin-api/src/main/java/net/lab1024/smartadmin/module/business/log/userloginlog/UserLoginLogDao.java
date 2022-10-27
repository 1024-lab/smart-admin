package net.lab1024.smartadmin.module.business.log.userloginlog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.module.business.log.userloginlog.domain.UserLoginLogQueryDTO;
import net.lab1024.smartadmin.module.business.log.userloginlog.domain.UserLoginLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [ 用户登录日志 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019-05-15 10:25:21
 * @since JDK1.8
 */
@Mapper
@Component
public interface UserLoginLogDao extends BaseMapper<UserLoginLogEntity> {

    /**
     * 分页查询
     * @param queryDTO
     * @return UserLoginLogEntity
    */
    List<UserLoginLogEntity> queryByPage(Page page, @Param("queryDTO") UserLoginLogQueryDTO queryDTO);

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
