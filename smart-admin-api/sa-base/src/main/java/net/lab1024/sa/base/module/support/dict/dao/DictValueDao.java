package net.lab1024.sa.base.module.support.dict.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.base.module.support.dict.domain.entity.DictValueEntity;
import net.lab1024.sa.base.module.support.dict.domain.form.DictValueQueryForm;
import net.lab1024.sa.base.module.support.dict.domain.vo.DictValueVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字典
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/5/26 19:40:55
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
@Component
public interface DictValueDao extends BaseMapper<DictValueEntity> {

    /**
     * 查找所有未删除的自带key
     *
     */
    List<DictValueEntity> selectByDeletedFlag(@Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查找所有未删除的自带key
     *
     */
    List<DictValueEntity> selectByDeletedFlagAndKeyId(@Param("dictKeyId") Long dictKeyId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 逻辑删除
     *
     */
    void updateDeletedFlagByIdList(@Param("dictValueIdList") List<Long> dictValueIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 分页查询
     *
     */
    List<DictValueVO> query(Page page, @Param("query") DictValueQueryForm queryForm);

    /**
     * 跟进code查询
     *
     */
    DictValueEntity selectByCode(@Param("valueCode") String valueCode, @Param("deletedFlag") Boolean deletedFlag);
}
