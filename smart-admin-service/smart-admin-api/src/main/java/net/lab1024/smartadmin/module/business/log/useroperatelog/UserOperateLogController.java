package net.lab1024.smartadmin.module.business.log.useroperatelog;

import net.lab1024.smartadmin.common.anno.OperateLog;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.business.log.useroperatelog.domain.UserOperateLogDTO;
import net.lab1024.smartadmin.module.business.log.useroperatelog.domain.UserOperateLogQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date 2019-05-15 11:32:14
 * @since JDK1.8
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_USER_OPERATE_LOG})
@OperateLog
public class UserOperateLogController {

    @Autowired
    private UserOperateLogService userOperateLogService;

    @ApiOperation(value = "分页查询",notes = "@author yandanyang")
    @PostMapping("/userOperateLog/page/query")
    public ResponseDTO<PageResultDTO<UserOperateLogDTO>> queryByPage(@RequestBody UserOperateLogQueryDTO queryDTO) {
        return userOperateLogService.queryByPage(queryDTO);
    }

    @ApiOperation(value="删除",notes = "@author yandanyang")
    @GetMapping("/userOperateLog/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable("id") Long id){
        return userOperateLogService.delete(id);
    }


    @ApiOperation(value="详情",notes = "@author yandanyang")
    @GetMapping("/userOperateLog/detail/{id}")
    public ResponseDTO<UserOperateLogDTO> detail(@PathVariable("id") Long id){
        return userOperateLogService.detail(id);
    }
}
