package net.lab1024.sa.admin.module.business.category.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.category.constant.CategoryTypeEnum;
import net.lab1024.sa.admin.module.business.category.domain.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 类目 dao
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021/08/05 21:26:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Component
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {

    /**
     * 根据父级id 类型 查询子类
     *
     * @param parentIdList 父级id集合
     * @param deletedFlag 删除标识
     * @return 列表
     */
    List<CategoryEntity> queryByParentId(@Param("parentIdList") List<Long> parentIdList,
                                         @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据父级id 类型 查询子类
     *
     * @param parentIdList 父级id集合
     * @param categoryType {@link CategoryTypeEnum}
     * @param deletedFlag 删除标识
     * @return 列表
     */
    List<CategoryEntity> queryByParentIdAndType(@Param("parentIdList") List<Long> parentIdList,
                                         @Param("categoryType") Integer categoryType,
                                         @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 某个类型的所有
     */
    List<CategoryEntity> queryByType(@Param("categoryType") Integer categoryType,
                                                @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据类型和id查询
     */
    CategoryEntity selectByTypeAndId(@Param("categoryType") Integer categoryType, @Param("categoryId") Long categoryId);

    /**
     * 查看类目 具体条件 看sql
     */
    CategoryEntity selectOne(CategoryEntity entity);

}
