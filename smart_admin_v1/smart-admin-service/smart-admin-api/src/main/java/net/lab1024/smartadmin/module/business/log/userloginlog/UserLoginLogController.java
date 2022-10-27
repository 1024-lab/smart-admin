package net.lab1024.smartadmin.module.business.log.userloginlog;

import net.lab1024.smartadmin.common.anno.OperateLog;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.module.system.employee.domain.dto.EmployeeQueryDTO;
import net.lab1024.smartadmin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.smartadmin.module.business.log.userloginlog.domain.UserLoginLogDTO;
import net.lab1024.smartadmin.module.business.log.userloginlog.domain.UserLoginLogQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * [ 用户登录日志 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date 2019-05-15 10:25:21
 * @since JDK1.8
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_USER_LOGIN_LOG})
@OperateLog
public class UserLoginLogController {

    @Autowired
    private UserLoginLogService userLoginLogService;

    @ApiOperation(value = "分页查询用户登录日志", notes = "@author yandanyang")
    @PostMapping("/userLoginLog/page/query")
    public ResponseDTO<PageResultDTO<UserLoginLogDTO>> queryByPage(@RequestBody UserLoginLogQueryDTO queryDTO) {
        return userLoginLogService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "删除用户登录日志", notes = "@author yandanyang")
    @GetMapping("/userLoginLog/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable("id") Long id) {
        return userLoginLogService.delete(id);
    }

    @ApiOperation(value = "查询员工在线状态", notes = "@author zzr")
    @PostMapping("/userOnLine/query")
    public ResponseDTO<PageResultDTO<EmployeeVO>> queryUserOnLine(@RequestBody @Valid EmployeeQueryDTO queryDTO) {
        return userLoginLogService.queryUserOnLine(queryDTO);
    }

}
