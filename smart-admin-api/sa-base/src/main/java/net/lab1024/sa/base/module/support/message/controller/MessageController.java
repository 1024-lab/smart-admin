package net.lab1024.sa.base.module.support.message.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.message.domain.MessageQueryForm;
import net.lab1024.sa.base.module.support.message.domain.MessageVO;
import net.lab1024.sa.base.module.support.message.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 消息
 *
 * @author luoyi
 * @date 2024/06/22 20:20
 */
@RestController
@Tag(name = SwaggerTagConst.Support.MESSAGE)
public class MessageController extends SupportBaseController {

    @Resource
    private MessageService messageService;

    @Operation(summary = "分页查询我的消息 @luoyi")
    @PostMapping("/message/queryMyMessage")
    public ResponseDTO<PageResult<MessageVO>> query(@RequestBody @Valid MessageQueryForm queryForm) {
        RequestUser user = SmartRequestUtil.getRequestUser();
        if(user == null){
            return ResponseDTO.userErrorParam("用户未登录");
        }

        queryForm.setSearchCount(false);
        queryForm.setReceiverUserId(user.getUserId());
        queryForm.setReceiverUserType(user.getUserType().getValue());
        return ResponseDTO.ok(messageService.query(queryForm));
    }

    @Operation(summary = "查询未读消息数量 @luoyi")
    @GetMapping("/message/getUnreadCount")
    public ResponseDTO<Long> getUnreadCount() {
        RequestUser user = SmartRequestUtil.getRequestUser();
        if(user == null){
            return ResponseDTO.userErrorParam("用户未登录");
        }
        return ResponseDTO.ok(messageService.getUnreadCount(user.getUserType(), user.getUserId()));
    }

    @Operation(summary = "更新已读 @luoyi")
    @GetMapping("/message/read/{messageId}")
    public ResponseDTO<String> updateReadFlag(@PathVariable Long messageId) {
        RequestUser user = SmartRequestUtil.getRequestUser();
        if(user == null){
            return ResponseDTO.userErrorParam("用户未登录");
        }

        messageService.updateReadFlag(messageId, user.getUserType(), user.getUserId());
        return ResponseDTO.ok();
    }

}
