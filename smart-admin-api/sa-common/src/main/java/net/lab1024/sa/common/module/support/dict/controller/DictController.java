package net.lab1024.sa.common.module.support.dict.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.sa.common.common.controller.SupportBaseController;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.constant.SwaggerTagConst;
import net.lab1024.sa.common.module.support.dict.service.DictCacheService;
import net.lab1024.sa.common.module.support.dict.service.DictService;
import net.lab1024.sa.common.module.support.dict.domain.form.*;
import net.lab1024.sa.common.module.support.dict.domain.vo.DictKeyVO;
import net.lab1024.sa.common.module.support.dict.domain.vo.DictValueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@Api(tags = {SwaggerTagConst.Support.DICT})
@RestController
public class DictController extends SupportBaseController {

    @Autowired
    private DictService dictService;
    @Autowired
    private DictCacheService dictCacheService;


    @ApiOperation("查询全部字典key - @author 卓大")
    @GetMapping("/dict/key/queryAll")
    public ResponseDTO<List<DictKeyVO>> queryAll() {
        return ResponseDTO.ok(dictService.queryAllKey());
    }

    @ApiOperation("分页查询数据字典value - @author 罗伊")
    @PostMapping("/dict/value/query")
    public ResponseDTO<PageResult<DictValueVO>> valueQuery(@Valid @RequestBody DictValueQueryForm queryForm) {
        return dictService.valueQuery(queryForm);
    }

    @ApiOperation("数据字典缓存-刷新- @author 罗伊")
    @GetMapping("/dict/cache/refresh")
    public ResponseDTO<String> cacheRefresh() {
        return dictCacheService.cacheRefresh();
    }

    @ApiOperation("数据字典-值列表- @author 罗伊")
    @GetMapping("/dict/value/list/{keyCode}")
    public ResponseDTO<List<DictValueVO>> valueList(@PathVariable String keyCode) {
        List<DictValueVO> dictValueVOList = dictCacheService.selectByKeyCode(keyCode);
        return ResponseDTO.ok(dictValueVOList);
    }
}
