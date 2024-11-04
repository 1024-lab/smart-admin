package net.lab1024.sa.base.module.support.reload.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.base.module.support.reload.domain.ReloadItemEntity;
import net.lab1024.sa.base.module.support.reload.domain.ReloadItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * t_reload_item 数据表dao
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2015-03-02 19:11:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Component
@Mapper
public interface ReloadItemDao extends BaseMapper<ReloadItemEntity> {

    List<ReloadItemVO> query();
}
