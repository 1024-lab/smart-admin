package net.lab1024.sa.admin.module.business.category.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.category.domain.form.CategoryAddForm;
import net.lab1024.sa.admin.module.business.category.domain.form.CategoryTreeQueryForm;
import net.lab1024.sa.admin.module.business.category.domain.form.CategoryUpdateForm;
import net.lab1024.sa.admin.module.business.category.domain.vo.CategoryTreeVO;
import net.lab1024.sa.admin.module.business.category.domain.vo.CategoryVO;
import net.lab1024.sa.admin.module.business.category.service.CategoryService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 类目
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021/08/05 21:26:58
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Tag(name = AdminSwaggerTagConst.Business.MANAGER_CATEGORY)
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Operation(summary = "添加类目 @author 胡克")
    @PostMapping("/category/add")
    @SaCheckPermission("category:add")
    public ResponseDTO<String> add(@RequestBody @Valid CategoryAddForm addForm) {
        return categoryService.add(addForm);
    }

    @Operation(summary = "更新类目 @author 胡克")
    @PostMapping("/category/update")
    @SaCheckPermission("category:update")
    public ResponseDTO<String> update(@RequestBody @Valid CategoryUpdateForm updateForm) {
        return categoryService.update(updateForm);
    }

    @Operation(summary = "查询类目详情 @author 胡克")
    @GetMapping("/category/{categoryId}")
    public ResponseDTO<CategoryVO> queryDetail(@PathVariable Long categoryId) {
        return categoryService.queryDetail(categoryId);
    }

    @Operation(summary = "查询类目层级树 @author 胡克")
    @PostMapping("/category/tree")
    @SaCheckPermission("category:tree")
    public ResponseDTO<List<CategoryTreeVO>> queryTree(@RequestBody @Valid CategoryTreeQueryForm queryForm) {
        return categoryService.queryTree(queryForm);
    }

    @Operation(summary = "删除类目 @author 胡克")
    @GetMapping("/category/delete/{categoryId}")
    @SaCheckPermission("category:delete")
    public ResponseDTO<String> delete(@PathVariable Long categoryId) {
        return categoryService.delete(categoryId);
    }
}
