package net.lab1024.sa.admin.module.system.support;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.cache.CacheService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 缓存
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021/10/11 20:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Tag(name = SwaggerTagConst.Support.CACHE)
public class AdminCacheController extends SupportBaseController {

    @Resource
    private CacheService cacheService;

    @Operation(summary = "获取所有缓存 @author 罗伊")
    @GetMapping("/cache/names")
    @SaCheckPermission("support:cache:keys")
    public ResponseDTO<List<String>> cacheNames() {
        return ResponseDTO.ok(cacheService.cacheNames());
    }

    @Operation(summary = "移除某个缓存 @author 罗伊")
    @GetMapping("/cache/remove/{cacheName}")
    @SaCheckPermission("support:cache:delete")
    public ResponseDTO<String> removeCache(@PathVariable String cacheName) {
        cacheService.removeCache(cacheName);
        return ResponseDTO.ok();
    }

    @Operation(summary = "获取某个缓存的所有key @author 罗伊")
    @GetMapping("/cache/keys/{cacheName}")
    @SaCheckPermission("support:cache:keys")
    public ResponseDTO<List<String>> cacheKeys(@PathVariable String cacheName) {
        return ResponseDTO.ok(cacheService.cacheKey(cacheName));
    }

}
