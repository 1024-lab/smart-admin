package net.lab1024.sa.admin.module.system.support;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.reload.ReloadService;
import net.lab1024.sa.base.module.support.reload.domain.ReloadForm;
import net.lab1024.sa.base.module.support.reload.domain.ReloadItemVO;
import net.lab1024.sa.base.module.support.reload.domain.ReloadResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * reload (内存热加载、钩子等)
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2015-03-02 19:11:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Tag(name = SwaggerTagConst.Support.RELOAD)
public class AdminReloadController extends SupportBaseController {

    @Resource
    private ReloadService reloadService;

    @Operation(summary = "查询reload列表 @author 开云")
    @GetMapping("/reload/query")
    public ResponseDTO<List<ReloadItemVO>> query() {
        return reloadService.query();
    }

    @Operation(summary = "获取reload result @author 开云")
    @GetMapping("/reload/result/{tag}")
    @SaCheckPermission("support:reload:result")
    public ResponseDTO<List<ReloadResultVO>> queryReloadResult(@PathVariable("tag") String tag) {
        return reloadService.queryReloadItemResult(tag);
    }

    @Operation(summary = "通过tag更新标识 @author 开云")
    @PostMapping("/reload/update")
    @SaCheckPermission("support:reload:update")
    public ResponseDTO<String> updateByTag(@RequestBody @Valid ReloadForm reloadForm) {
        return reloadService.updateByTag(reloadForm);
    }
}
