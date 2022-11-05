package net.lab1024.sa.common.module.support.dict.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.common.module.support.dict.domain.entity.DictKeyEntity;
import net.lab1024.sa.common.module.support.dict.domain.form.DictKeyQueryForm;
import net.lab1024.sa.common.module.support.dict.domain.vo.DictKeyVO;
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
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Mapper
@Component
public interface DictKeyDao extends BaseMapper<DictKeyEntity> {

    /**
     * 查找所有未删除的自带key
     * @param deletedFlag
     * @return
     */
    List<DictKeyEntity> selectByDeletedFlag(@Param("deletedFlag") Boolean deletedFlag);

    /**
     * 逻辑删除
     *
     * @param dictKeyIdList
     * @param deletedFlag
     */
    void updateDeletedFlagByIdList(@Param("dictKeyIdList") List<Long> dictKeyIdList, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<DictKeyVO> query(Page page, @Param("query") DictKeyQueryForm queryForm);

    /**
     * 跟进code查询
     * @param keyCode
     * @param deletedFlag
     * @return
     */
    DictKeyEntity selectByCode(@Param("keyCode")String keyCode, @Param("deletedFlag") Boolean deletedFlag);
}
