package net.lab1024.sa.admin.module.system.support;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.sa.common.common.controller.SupportBaseController;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.constant.SwaggerTagConst;
import net.lab1024.sa.common.module.support.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 缓存
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021/10/11 20:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@RestController
@Api(tags = {SwaggerTagConst.Support.CACHE})
public class AdminCacheController extends SupportBaseController {

    @Autowired
    private CacheService cacheService;

    @ApiOperation(value = "获取所有缓存 @author 罗伊")
    @GetMapping("/cache/names")
    public ResponseDTO<List<String>> cacheNames() {
        return ResponseDTO.ok(cacheService.cacheNames());
    }


    @ApiOperation(value = "移除某个缓存 @author 罗伊")
    @PreAuthorize("@saAuth.checkPermission('support:cache:delete')")
    @GetMapping("/cache/remove/{cacheName}")
    public ResponseDTO<String> removeCache(@PathVariable String cacheName) {
        cacheService.removeCache(cacheName);
        return ResponseDTO.ok();
    }


    @ApiOperation(value = "获取某个缓存的所有key @author 罗伊")
    @PreAuthorize("@saAuth.checkPermission('support:cache:keys')")
    @GetMapping("/cache/keys/{cacheName}")
    public ResponseDTO<List<String>> cacheKeys(@PathVariable String cacheName) {
        return ResponseDTO.ok(cacheService.cacheKey(cacheName));
    }

}
