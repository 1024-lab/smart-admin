package net.lab1024.sa.admin.module.business.category.service;

import com.google.common.collect.Lists;
import net.lab1024.sa.admin.module.business.category.dao.CategoryDao;
import net.lab1024.sa.admin.module.business.category.domain.entity.CategoryEntity;
import net.lab1024.sa.admin.module.business.category.domain.form.CategoryAddForm;
import net.lab1024.sa.admin.module.business.category.domain.form.CategoryTreeQueryForm;
import net.lab1024.sa.admin.module.business.category.domain.form.CategoryUpdateForm;
import net.lab1024.sa.admin.module.business.category.domain.vo.CategoryTreeVO;
import net.lab1024.sa.admin.module.business.category.domain.vo.CategoryVO;
import net.lab1024.sa.admin.module.business.category.manager.CategoryCacheManager;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 类目
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021/08/05 21:26:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private CategoryQueryService categoryQueryService;

    @Resource
    private CategoryCacheManager categoryCacheManager;

    /**
     * 添加类目
     */
    public ResponseDTO<String> add(CategoryAddForm addForm) {
        // 校验类目
        CategoryEntity categoryEntity = SmartBeanUtil.copy(addForm, CategoryEntity.class);
        ResponseDTO<String> res = this.checkCategory(categoryEntity, false);
        if (!res.getOk()) {
            return res;
        }
        // 没有父类则使用默认父类
        Long parentId = null == addForm.getParentId() ? NumberUtils.LONG_ZERO : addForm.getParentId();
        categoryEntity.setParentId(parentId);
        categoryEntity.setSort(null == addForm.getSort() ? 0 : addForm.getSort());
        categoryEntity.setDeletedFlag(false);

        // 保存数据
        categoryDao.insert(categoryEntity);

        // 更新缓存
        categoryCacheManager.removeCache();
        return ResponseDTO.ok();
    }

    /**
     * 更新类目
     * 不能更新父级类目
     *
     */
    public ResponseDTO<String> update(CategoryUpdateForm updateForm) {
        // 校验类目
        Long categoryId = updateForm.getCategoryId();
        Optional<CategoryEntity> optional = categoryQueryService.queryCategory(categoryId);
        if (!optional.isPresent()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        CategoryEntity categoryEntity = SmartBeanUtil.copy(updateForm, CategoryEntity.class);

        /*
          不更新类目类型
          不更新父类id
         */
        Integer categoryType = optional.get().getCategoryType();
        categoryEntity.setCategoryType(categoryType);
        categoryEntity.setParentId(optional.get().getParentId());

        ResponseDTO<String> responseDTO = this.checkCategory(categoryEntity, true);
        if (!responseDTO.getOk()) {
            return responseDTO;
        }
        categoryDao.updateById(categoryEntity);

        // 更新缓存
        categoryCacheManager.removeCache();
        return ResponseDTO.ok();
    }

    /**
     * 新增/更新 类目时的 校验
     *
     */
    private ResponseDTO<String> checkCategory(CategoryEntity categoryEntity, boolean isUpdate) {
        // 校验父级是否存在
        Long parentId = categoryEntity.getParentId();
        Integer categoryType = categoryEntity.getCategoryType();
        if (null != parentId) {
            if (Objects.equals(categoryEntity.getCategoryId(), parentId)) {
                return ResponseDTO.userErrorParam("父级类目怎么和自己相同了");
            }
            if (!Objects.equals(parentId, NumberUtils.LONG_ZERO)) {
                Optional<CategoryEntity> optional = categoryQueryService.queryCategory(parentId);
                if (!optional.isPresent()) {
                    return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST, "父级类目不存在~");
                }

                CategoryEntity parent = optional.get();
                if (!Objects.equals(categoryType, parent.getCategoryType())) {
                    return ResponseDTO.userErrorParam("与父级类目类型不一致");
                }
            }

        } else {
            // 如果没有父类 使用默认父类
            parentId = NumberUtils.LONG_ZERO;
        }

        // 校验同父类下 名称是否重复
        CategoryEntity queryEntity = new CategoryEntity();
        queryEntity.setParentId(parentId);
        queryEntity.setCategoryType(categoryType);
        queryEntity.setCategoryName(categoryEntity.getCategoryName());
        queryEntity.setDeletedFlag(false);
        queryEntity = categoryDao.selectOne(queryEntity);
        if (null != queryEntity) {
            if (isUpdate) {
                if (!Objects.equals(queryEntity.getCategoryId(), categoryEntity.getCategoryId())) {
                    return ResponseDTO.userErrorParam("同级下已存在相同类目~");
                }
            } else {
                return ResponseDTO.userErrorParam("同级下已存在相同类目~");
            }
        }
        return ResponseDTO.ok();
    }

    /**
     * 查询 类目详情
     *
     */
    public ResponseDTO<CategoryVO> queryDetail(Long categoryId) {
        Optional<CategoryEntity> optional = categoryQueryService.queryCategory(categoryId);
        if (!optional.isPresent()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        CategoryVO adminVO = SmartBeanUtil.copy(optional.get(), CategoryVO.class);
        return ResponseDTO.ok(adminVO);
    }

    /**
     * 根据父级id 查询所有子类 返回层级树
     * 如果父类id 为空 返回所有类目层级
     *
     */
    public ResponseDTO<List<CategoryTreeVO>> queryTree(CategoryTreeQueryForm queryForm) {
        if (null == queryForm.getParentId()) {
            if (null == queryForm.getCategoryType()) {
                return ResponseDTO.userErrorParam("类目类型不能为空");
            }
            queryForm.setParentId(NumberUtils.LONG_ZERO);
        }
        List<CategoryTreeVO> treeList = categoryCacheManager.queryCategoryTree(queryForm.getParentId(), queryForm.getCategoryType());
        return ResponseDTO.ok(treeList);
    }

    /**
     * 删除类目
     * 如果有未删除的子类 则无法删除
     *
     */
    public ResponseDTO<String> delete(Long categoryId) {
        Optional<CategoryEntity> optional = categoryQueryService.queryCategory(categoryId);
        if (!optional.isPresent()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        List<Long> categorySubId = categoryQueryService.queryCategorySubId(Lists.newArrayList(categoryId));
        if (CollectionUtils.isNotEmpty(categorySubId)) {
            return ResponseDTO.userErrorParam("请先删除子级类目");
        }

        // 更新数据
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(categoryId);
        categoryEntity.setDeletedFlag(true);
        categoryDao.updateById(categoryEntity);

        // 更新缓存
        categoryCacheManager.removeCache();
        return ResponseDTO.ok();
    }

}
