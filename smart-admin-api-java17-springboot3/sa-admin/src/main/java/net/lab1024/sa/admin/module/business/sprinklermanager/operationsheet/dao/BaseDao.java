package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BaseDao<E> extends BaseMapper<E> {
}
