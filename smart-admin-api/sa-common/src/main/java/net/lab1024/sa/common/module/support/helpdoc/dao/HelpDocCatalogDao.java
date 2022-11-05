package net.lab1024.sa.common.module.support.helpdoc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.common.module.support.helpdoc.domain.entity.HelpDocCatalogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 帮助文档目录
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-20 23:11:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Mapper
@Component
public interface HelpDocCatalogDao extends BaseMapper<HelpDocCatalogEntity> {

}
