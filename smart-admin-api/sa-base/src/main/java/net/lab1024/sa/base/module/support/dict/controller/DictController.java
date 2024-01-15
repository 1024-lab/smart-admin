package net.lab1024.sa.base.module.support.dict.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.dict.domain.form.DictValueQueryForm;
import net.lab1024.sa.base.module.support.dict.domain.vo.DictKeyVO;
import net.lab1024.sa.base.module.support.dict.domain.vo.DictValueVO;
import net.lab1024.sa.base.module.support.dict.service.DictCacheService;
import net.lab1024.sa.base.module.support.dict.service.DictService;
import org.springframework.web.bind.annotation.*;

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
public class DictController extends SupportBaseController {

    @Resource
    private DictService dictService;
    @Resource
    private DictCacheService dictCacheService;


    @Operation(summary = "查询全部字典key - @author 卓大")
    @GetMapping("/dict/key/queryAll")
    public ResponseDTO<List<DictKeyVO>> queryAll() {
        return ResponseDTO.ok(dictService.queryAllKey());
    }

    @Operation(summary = "分页查询数据字典value - @author 罗伊")
    @PostMapping("/dict/value/query")
    public ResponseDTO<PageResult<DictValueVO>> valueQuery(@Valid @RequestBody DictValueQueryForm queryForm) {
        return dictService.valueQuery(queryForm);
    }

    @Operation(summary = "数据字典-值列表- @author 罗伊")
    @GetMapping("/dict/value/list/{keyCode}")
    public ResponseDTO<List<DictValueVO>> valueList(@PathVariable String keyCode) {
        List<DictValueVO> dictValueVOList = dictCacheService.selectByKeyCode(keyCode);
        return ResponseDTO.ok(dictValueVOList);
    }
}
