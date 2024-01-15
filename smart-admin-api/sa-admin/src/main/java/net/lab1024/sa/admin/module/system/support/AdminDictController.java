package net.lab1024.sa.admin.module.system.support;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.dict.domain.form.*;
import net.lab1024.sa.base.module.support.dict.domain.vo.DictKeyVO;
import net.lab1024.sa.base.module.support.dict.service.DictCacheService;
import net.lab1024.sa.base.module.support.dict.service.DictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
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
@Tag(name = SwaggerTagConst.Support.DICT)
@RestController
public class AdminDictController extends SupportBaseController {

    @Resource
    private DictService dictService;

    @Resource
    private DictCacheService dictCacheService;

    @Operation(summary = "分页查询数据字典KEY - @author 罗伊")
    @PostMapping("/dict/key/query")
    public ResponseDTO<PageResult<DictKeyVO>> keyQuery(@Valid @RequestBody DictKeyQueryForm queryForm) {
        return dictService.keyQuery(queryForm);
    }


    @Operation(summary = "数据字典KEY-添加- @author 罗伊")
    @PostMapping("/dict/key/add")
    @SaCheckPermission("support:dict:add")
    public ResponseDTO<String> keyAdd(@Valid @RequestBody DictKeyAddForm keyAddForm) {
        return dictService.keyAdd(keyAddForm);
    }

    @Operation(summary = "数据字典缓存-刷新- @author 罗伊")
    @GetMapping("/dict/cache/refresh")
    @SaCheckPermission("support:dict:refresh")
    public ResponseDTO<String> cacheRefresh() {
        return dictCacheService.cacheRefresh();
    }

    @Operation(summary = "数据字典Value-添加- @author 罗伊")
    @PostMapping("/dict/value/add")
    public ResponseDTO<String> valueAdd(@Valid @RequestBody DictValueAddForm valueAddForm) {
        return dictService.valueAdd(valueAddForm);
    }

    @Operation(summary = "数据字典KEY-更新- @author 罗伊")
    @PostMapping("/dict/key/edit")
    @SaCheckPermission("support:dict:edit")
    public ResponseDTO<String> keyEdit(@Valid @RequestBody DictKeyUpdateForm keyUpdateForm) {
        return dictService.keyEdit(keyUpdateForm);
    }

    @Operation(summary = "数据字典Value-更新- @author 罗伊")
    @PostMapping("/dict/value/edit")
    public ResponseDTO<String> valueEdit(@Valid @RequestBody DictValueUpdateForm valueUpdateForm) {
        return dictService.valueEdit(valueUpdateForm);
    }

    @Operation(summary = "数据字典KEY-删除- @author 罗伊")
    @PostMapping("/dict/key/delete")
    @SaCheckPermission("support:dict:delete")
    public ResponseDTO<String> keyDelete(@RequestBody List<Long> keyIdList) {
        return dictService.keyDelete(keyIdList);
    }

    @Operation(summary = "数据字典Value-删除- @author 罗伊")
    @PostMapping("/dict/value/delete")
    public ResponseDTO<String> valueDelete(@RequestBody List<Long> valueIdList) {
        return dictService.valueDelete(valueIdList);
    }

}
