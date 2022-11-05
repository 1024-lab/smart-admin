package net.lab1024.sa.admin.module.system.support;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.sa.common.common.controller.SupportBaseController;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.constant.SwaggerTagConst;
import net.lab1024.sa.common.module.support.operatelog.OperateLogService;
import net.lab1024.sa.common.module.support.operatelog.domain.OperateLogQueryForm;
import net.lab1024.sa.common.module.support.operatelog.domain.OperateLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *  操作日志
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021-12-08 20:48:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@RestController
@Api(tags = {SwaggerTagConst.Support.OPERATE_LOG})
public class AdminOperateLogController extends SupportBaseController {

    @Autowired
    private OperateLogService operateLogService;

    @ApiOperation(value = "分页查询 @author 罗伊")
    @PreAuthorize("@saAuth.checkPermission('operateLog:query')")
    @PostMapping("/operateLog/page/query")
    public ResponseDTO<PageResult<OperateLogVO>> queryByPage(@RequestBody OperateLogQueryForm queryForm) {
        return operateLogService.queryByPage(queryForm);
    }

    @ApiOperation(value = "详情 @author 罗伊")
    @PreAuthorize("@saAuth.checkPermission('operateLog:detail')")
    @GetMapping("/operateLog/detail/{operateLogId}")
    public ResponseDTO<OperateLogVO> detail(@PathVariable Long operateLogId) {
        return operateLogService.detail(operateLogId);
    }

}
