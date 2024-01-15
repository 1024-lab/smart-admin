package net.lab1024.sa.admin.module.system.support;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.ValidateList;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.securityprotect.domain.LoginFailQueryForm;
import net.lab1024.sa.base.module.support.securityprotect.domain.LoginFailVO;
import net.lab1024.sa.base.module.support.securityprotect.service.ProtectLoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 *
 * 网络安全
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2023/10/17 19:07:27
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>，Since 2012
 */

@RestController
@Tag(name = SwaggerTagConst.Support.PROTECT)
public class AdminProtectController extends SupportBaseController {

    @Resource
    private ProtectLoginService protectLoginService;

    @Operation(summary = "分页查询 @author 1024创新实验室-主任-卓大")
    @PostMapping("/protect/loginFail/queryPage")
    public ResponseDTO<PageResult<LoginFailVO>> queryPage(@RequestBody @Valid LoginFailQueryForm queryForm) {
        return ResponseDTO.ok(protectLoginService.queryPage(queryForm));
    }


    @Operation(summary = "批量删除 @author 1024创新实验室-主任-卓大")
    @PostMapping("/protect/loginFail/batchDelete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return protectLoginService.batchDelete(idList);
    }


}
