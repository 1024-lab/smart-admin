package net.lab1024.sa.base.module.support.helpdoc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.base.module.support.helpdoc.domain.entity.HelpDocCatalogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 帮助文档目录
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-20 23:11:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
@Component
public interface HelpDocCatalogDao extends BaseMapper<HelpDocCatalogEntity> {

}
