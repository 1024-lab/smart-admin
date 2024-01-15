package net.lab1024.sa.admin.module.business.category.service;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.category.domain.dto.CategorySimpleDTO;
import net.lab1024.sa.admin.module.business.category.domain.entity.CategoryEntity;
import net.lab1024.sa.admin.module.business.category.manager.CategoryCacheManager;
import net.lab1024.sa.base.common.constant.StringConst;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 类目 查询 业务类
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021/08/05 21:26:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
@Slf4j
public class CategoryQueryService {

    private static final Long DEFAULT_CATEGORY_PARENT_ID = 0L;

    @Resource
    private CategoryCacheManager categoryCacheManager;

    /**
     * 根据 id 查询未删除的类目
     *
     * @param categoryId
     * @return 可能 null
     */
    public Optional<CategoryEntity> queryCategory(Long categoryId) {
        if (null == categoryId) {
            return Optional.empty();
        }
        CategoryEntity entity = categoryCacheManager.queryCategory(categoryId);
        if (null == entity || entity.getDeletedFlag()) {
            return Optional.empty();
        }
        return Optional.of(entity);
    }


    /**
     * 根据 类目id集合 查询未删除的类目集合
     */
    public Map<Long, CategoryEntity> queryCategoryList(List<Long> categoryIdList) {
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Collections.emptyMap();
        }
        categoryIdList = categoryIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, CategoryEntity> categoryEntityMap = Maps.newHashMap();
        for (Long categoryId : categoryIdList) {
            CategoryEntity categoryEntity = categoryCacheManager.queryCategory(categoryId);
            if (categoryEntity != null) {
                categoryEntityMap.put(categoryId, categoryEntity);
            }
        }
        return categoryEntityMap;
    }


    /**
     * 根据类目id 递归查询该id的所有子类id 递归查询
     * 同时存入缓存
     * 注意：查询出来的集合 不包含传递的父类参数
     */
    public List<Long> queryCategorySubId(List<Long> categoryIdList) {
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Collections.emptyList();
        }
        //所有子类
        List<CategoryEntity> categoryEntityList = Lists.newArrayList();
        categoryIdList.forEach(e -> {
            categoryEntityList.addAll(categoryCacheManager.querySubCategory(e));
        });
        Map<Long, List<CategoryEntity>> subTypeMap = categoryEntityList.stream().collect(Collectors.groupingBy(CategoryEntity::getCategoryId));
        // 递归查询子类
        categoryIdList = subTypeMap.values().stream().flatMap(Collection::stream).map(CategoryEntity::getCategoryId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Lists.newArrayList();
        }
        categoryIdList.addAll(this.queryCategorySubId(categoryIdList));
        return categoryIdList;
    }


    /**
     * 处理类目名称
     */
    public List<String> queryCategoryName(List<Long> categoryIdList) {
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return null;
        }
        Map<Long, CategoryEntity> categoryMap = this.queryCategoryList(categoryIdList);
        List<String> categoryNameList = Lists.newArrayList();
        categoryIdList.forEach(e -> {
            CategoryEntity categoryEntity = categoryMap.get(e);
            if (categoryEntity != null) {
                categoryNameList.add(categoryMap.get(e).getCategoryName());
            }
        });
        return categoryNameList;
    }

    /**
     * 根据类目id 查询类目名称
     */
    public String queryCategoryName(Long categoryId) {
        CategoryEntity categoryEntity = categoryCacheManager.queryCategory(categoryId);
        if (null == categoryEntity || categoryEntity.getDeletedFlag()) {
            return null;
        }
        return categoryEntity.getCategoryName();
    }

    /**
     * 根据类目id 查询类目详情 包含类目全称 如：医考/医师资格/临床执业
     */
    public CategorySimpleDTO queryCategoryInfo(Long categoryId) {
        CategoryEntity categoryEntity = categoryCacheManager.queryCategory(categoryId);
        if (null == categoryEntity || categoryEntity.getDeletedFlag()) {
            return null;
        }
        String fullName = this.queryFullName(categoryId);
        // 返回DTO
        CategorySimpleDTO categoryDTO = new CategorySimpleDTO();
        categoryDTO.setCategoryId(categoryId);
        categoryDTO.setCategoryName(categoryEntity.getCategoryName());
        categoryDTO.setCategoryFullName(fullName);
        categoryDTO.setParentId(categoryEntity.getParentId());
        return categoryDTO;
    }

    /**
     * 递归查询分类和所有父级类目
     * ps:特别注意返回的集合中 包含自己
     */
    public List<CategoryEntity> queryCategoryAndParent(Long categoryId) {
        List<CategoryEntity> parentCategoryList = Lists.newArrayList();
        CategoryEntity categoryEntity = categoryCacheManager.queryCategory(categoryId);
        if (null == categoryEntity || categoryEntity.getDeletedFlag()) {
            return parentCategoryList;
        }

        // 父级始终放在第一位
        parentCategoryList.add(0, categoryEntity);
        Long parentId = categoryEntity.getParentId();
        if (Objects.equals(DEFAULT_CATEGORY_PARENT_ID, parentId)) {
            return parentCategoryList;
        }
        parentCategoryList.addAll(0, this.queryCategoryAndParent(parentId));
        return parentCategoryList;
    }

    /**
     * 查询 分类全称 如：医考/医师资格/临床执业
     */
    public String queryFullName(Long categoryId) {
        List<CategoryEntity> parentCategoryList = this.queryCategoryAndParent(categoryId);
        // 拼接父级类目名称 斜杠分隔返回
        List<String> nameList = parentCategoryList.stream().map(CategoryEntity::getCategoryName).collect(Collectors.toList());
        return StrUtil.join(StringConst.SEPARATOR_SLASH, nameList);
    }

    /**
     * 查询 分类全称 如：医考/医师资格/临床执业
     */
    public Map<Long, String> queryFullName(List<Long> categoryIdList) {
        if (CollectionUtils.isEmpty(categoryIdList)) {
            return Maps.newHashMap();
        }
        // 循环内查询的缓存 还ok
        return categoryIdList.stream().collect(Collectors.toMap(Function.identity(), this::queryFullName));
    }

}
