package net.lab1024.sa.common.module.support.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.common.module.support.config.domain.ConfigEntity;
import net.lab1024.sa.common.module.support.config.domain.ConfigQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统参数配置 t_config Dao层
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-14 20:46:27
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Component
@Mapper
public interface ConfigDao extends BaseMapper<ConfigEntity> {

    /**
     * 分页查询系统配置
     *
     * @param page
     * @return
     */
    List<ConfigEntity> queryByPage(Page page, @Param("query") ConfigQueryForm queryDTO);

    /**
     * 根据key查询获取数据
     *
     * @param key
     * @return
     */
    ConfigEntity selectByKey(String key);
}
