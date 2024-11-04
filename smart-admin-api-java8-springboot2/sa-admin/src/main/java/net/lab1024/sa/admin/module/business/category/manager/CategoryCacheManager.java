package net.lab1024.sa.admin.module.business.category.manager;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminCacheConst;
import net.lab1024.sa.admin.module.business.category.dao.CategoryDao;
import net.lab1024.sa.admin.module.business.category.domain.entity.CategoryEntity;
import net.lab1024.sa.admin.module.business.category.domain.vo.CategoryTreeVO;
import net.lab1024.sa.base.common.constant.StringConst;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类目 查询 缓存
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021/08/05 21:26:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
@Slf4j
public class CategoryCacheManager {


    @Resource
    private CategoryDao categoryDao;


    /**
     * 根据类目id 移除缓存
     */
    @CacheEvict(value = {AdminCacheConst.Category.CATEGORY_ENTITY, AdminCacheConst.Category.CATEGORY_SUB, AdminCacheConst.Category.CATEGORY_TREE}, allEntries = true)
    public void removeCache() {
        log.info("clear CATEGORY ,CATEGORY_SUB ,CATEGORY_TREE");
    }

    /**
     * 查詢类目
     *
     */
    @Cacheable(AdminCacheConst.Category.CATEGORY_ENTITY)
    public CategoryEntity queryCategory(Long categoryId) {
        return categoryDao.selectById(categoryId);
    }

    /**
     * 查询类目 子级
     *
     */
    @Cacheable(AdminCacheConst.Category.CATEGORY_SUB)
    public List<CategoryEntity> querySubCategory(Long categoryId) {
        return categoryDao.queryByParentId(Lists.newArrayList(categoryId), false);
    }


    /**
     * 查询类目 层级树
     * 优先查询缓存
     */
    @Cacheable(AdminCacheConst.Category.CATEGORY_TREE)
    public List<CategoryTreeVO> queryCategoryTree(Long parentId, Integer categoryType) {
        List<CategoryEntity> allCategoryEntityList = categoryDao.queryByType(categoryType, false);

        List<CategoryEntity> categoryEntityList = allCategoryEntityList.stream().filter(e -> e.getParentId().equals(parentId)).collect(Collectors.toList());
        List<CategoryTreeVO> treeList = SmartBeanUtil.copyList(categoryEntityList, CategoryTreeVO.class);
        treeList.forEach(e -> {
            e.setLabel(e.getCategoryName());
            e.setValue(e.getCategoryId());
            e.setCategoryFullName(e.getCategoryName());
        });
        // 递归设置子类
        this.queryAndSetSubCategory(treeList, allCategoryEntityList);
        return treeList;
    }

    /**
     * 递归查询设置类目子类
     * 从缓存查询子类
     *
     */
    private void queryAndSetSubCategory(List<CategoryTreeVO> treeList, List<CategoryEntity> allCategoryEntityList) {
        if (CollectionUtils.isEmpty(treeList)) {
            return;
        }
        List<Long> parentIdList = treeList.stream().map(CategoryTreeVO::getValue).collect(Collectors.toList());
        List<CategoryEntity> categoryEntityList = allCategoryEntityList.stream().filter(e -> parentIdList.contains(e.getParentId())).collect(Collectors.toList());
        Map<Long, List<CategoryEntity>> categorySubMap = categoryEntityList.stream().collect(Collectors.groupingBy(CategoryEntity::getParentId));
        treeList.forEach(e -> {
            List<CategoryEntity> childrenEntityList = categorySubMap.getOrDefault(e.getValue(), Lists.newArrayList());
            List<CategoryTreeVO> childrenVOList = SmartBeanUtil.copyList(childrenEntityList, CategoryTreeVO.class);
            childrenVOList.forEach(item -> {
                item.setLabel(item.getCategoryName());
                item.setValue(item.getCategoryId());
                item.setCategoryFullName(e.getCategoryFullName() + StringConst.SEPARATOR_SLASH + item.getCategoryName());
            });
            // 递归查询
            this.queryAndSetSubCategory(childrenVOList, allCategoryEntityList);
            e.setChildren(childrenVOList);
        });
    }


}
