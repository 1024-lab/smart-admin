package net.lab1024.sa.admin.module.system.support;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.sa.common.common.controller.SupportBaseController;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.constant.SwaggerTagConst;
import net.lab1024.sa.common.module.support.dict.domain.form.*;
import net.lab1024.sa.common.module.support.dict.domain.vo.DictKeyVO;
import net.lab1024.sa.common.module.support.dict.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
public class AdminDictController extends SupportBaseController {

    @Autowired
    private DictService dictService;

    @ApiOperation("分页查询数据字典KEY - @author 罗伊")
    @PostMapping("/dict/key/query")
    public ResponseDTO<PageResult<DictKeyVO>> keyQuery(@Valid @RequestBody DictKeyQueryForm queryForm) {
        return dictService.keyQuery(queryForm);
    }


    @ApiOperation("数据字典KEY-添加- @author 罗伊")
    @PostMapping("/dict/key/add")
    public ResponseDTO<String> keyAdd(@Valid @RequestBody DictKeyAddForm keyAddForm) {
        return dictService.keyAdd(keyAddForm);
    }

    @ApiOperation("数据字典Value-添加- @author 罗伊")
    @PostMapping("/dict/value/add")
    public ResponseDTO<String> valueAdd(@Valid @RequestBody DictValueAddForm valueAddForm) {
        return dictService.valueAdd(valueAddForm);
    }

    @ApiOperation("数据字典KEY-更新- @author 罗伊")
    @PostMapping("/dict/key/edit")
    public ResponseDTO<String> keyEdit(@Valid @RequestBody DictKeyUpdateForm keyUpdateForm) {
        return dictService.keyEdit(keyUpdateForm);
    }

    @ApiOperation("数据字典Value-更新- @author 罗伊")
    @PostMapping("/dict/value/edit")
    public ResponseDTO<String> valueEdit(@Valid @RequestBody DictValueUpdateForm valueUpdateForm) {
        return dictService.valueEdit(valueUpdateForm);
    }

    @ApiOperation("数据字典KEY-删除- @author 罗伊")
    @PostMapping("/dict/key/delete")
    public ResponseDTO<String> keyDelete(@RequestBody List<Long> keyIdList) {
        return dictService.keyDelete(keyIdList);
    }

    @ApiOperation("数据字典Value-删除- @author 罗伊")
    @PostMapping("/dict/value/delete")
    public ResponseDTO<String> valueDelete(@RequestBody List<Long> valueIdList) {
        return dictService.valueDelete(valueIdList);
    }

}
