package net.lab1024.smartadmin.module.business.email;

import net.lab1024.smartadmin.common.anno.NoValidPrivilege;
import net.lab1024.smartadmin.common.anno.OperateLog;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.business.email.domain.dto.EmailDTO;
import net.lab1024.smartadmin.module.business.email.domain.dto.EmailQueryDTO;
import net.lab1024.smartadmin.module.business.email.domain.dto.EmailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date 2019-05-13 17:10:16
 * @since JDK1.8
 */
@RestController
@OperateLog
@Api(tags = {SwaggerTagConst.Admin.MANAGER_EMAIL})
public class EmailController {

    @Autowired
    private EmailService emailService;

    @ApiOperation(value = "分页查询",notes = "@author yandanyang")
    @PostMapping("/email/page/query")
    @NoValidPrivilege
    public ResponseDTO<PageResultDTO<EmailVO>> queryByPage(@RequestBody @Validated EmailQueryDTO queryDTO) {
        return emailService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "添加",notes = "@author yandanyang")
    @PostMapping("/email/add")
    @NoValidPrivilege
    public ResponseDTO<Long> add(@RequestBody @Valid EmailDTO addTO){
        return emailService.add(addTO);
    }

    @ApiOperation(value="修改",notes = "@author yandanyang")
    @PostMapping("/email/update")
    @NoValidPrivilege
    public ResponseDTO<Long> update(@RequestBody @Valid EmailDTO updateDTO){
        return emailService.update(updateDTO);
    }


    @ApiOperation(value="删除",notes = "@author yandanyang")
    @GetMapping("/email/delete/{id}")
    @NoValidPrivilege
    public ResponseDTO<String> delete(@PathVariable("id") Long id){
        return emailService.delete(id);
    }


    @ApiOperation(value="详情",notes = "@author yandanyang")
    @GetMapping("/email/detail/{id}")
    @NoValidPrivilege
    public ResponseDTO<EmailVO> detail(@PathVariable("id") Long id){
        return emailService.detail(id);
    }


    @ApiOperation(value="发送",notes = "@author yandanyang")
    @GetMapping("/email/send/{id}")
    @NoValidPrivilege
    public ResponseDTO<String> send(@PathVariable("id") Long id){
        return emailService.send(id);
    }
}
