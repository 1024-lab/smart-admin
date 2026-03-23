package net.lab1024.sa.admin.module.system.message;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.ValidateList;
import net.lab1024.sa.base.module.support.message.domain.MessageQueryForm;
import net.lab1024.sa.base.module.support.message.domain.MessageSendForm;
import net.lab1024.sa.base.module.support.message.domain.MessageVO;
import net.lab1024.sa.base.module.support.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 后管 消息路由
 *
 * @author: 卓大
 * @date: 2025/04/09 20:55
 */
@Tag(name = AdminSwaggerTagConst.System.SYSTEM_MESSAGE)
@RestController
public class AdminMessageController {

    @Autowired
    private MessageService messageService;

    @Operation(summary = "通知消息-新建  @author 卓大")
    @PostMapping("/message/sendMessages")
    @SaCheckPermission("system:message:send")
    public ResponseDTO<String> sendMessages(@RequestBody @Valid ValidateList<MessageSendForm> messageList) {
        messageService.sendMessage(messageList);
        return ResponseDTO.ok();
    }

    @Operation(summary = "通知消息-分页查询   @author 卓大")
    @PostMapping("/message/query")
    @SaCheckPermission("system:message:query")
    public ResponseDTO<PageResult<MessageVO>> query(@RequestBody @Valid MessageQueryForm queryForm) {
        return ResponseDTO.ok(messageService.query(queryForm));
    }

    @Operation(summary = "通知消息-删除   @author 卓大")
    @GetMapping("/message/delete/{messageId}")
    @SaCheckPermission("system:message:delete")
    public ResponseDTO<String> delete(@PathVariable Long messageId) {
        return messageService.delete(messageId);
    }

}
