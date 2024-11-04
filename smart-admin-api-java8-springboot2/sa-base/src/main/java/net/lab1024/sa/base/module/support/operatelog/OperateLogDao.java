package net.lab1024.sa.base.module.support.operatelog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.base.module.support.operatelog.domain.OperateLogEntity;
import net.lab1024.sa.base.module.support.operatelog.domain.OperateLogQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  操作日志
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021-12-08 20:48:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
@Component
public interface OperateLogDao extends BaseMapper<OperateLogEntity> {

    /**
     * 分页查询
     * @param page
     * @param queryForm
     * @return UserOperateLogEntity
     */
    List<OperateLogEntity> queryByPage(Page page, @Param("query") OperateLogQueryForm queryForm);


    /**
     * 批量删除
     *
     * @param idList
     * @return
     */
    void deleteByIds(@Param("idList") List<Long> idList);
}
