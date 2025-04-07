package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@OperateLog
public class OperationSheetController {

    @Resource
    private OperationSheetService operationSheetService;

    @Operation(summary = "批量新建入库操作记录表 @author 芦苇")
    @PostMapping("/stockinoperationsheet/create")
    @SaCheckPermission("stockinoperationsheet:add")
    public ResponseDTO<String> createStockInOperationSheet(@RequestPart("file") @Valid MultipartFile file) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return operationSheetService.batchCreateStockInOperationSheet(file, requestUser);
    }

    @Operation(summary = "批量新建领用操作记录表 @author 芦苇")
    @PostMapping("/allocateoperationsheet/create")
    @SaCheckPermission("allocateoperationsheet:add")
    public ResponseDTO<String> createAllocateOperationSheet(@RequestPart("file") @Valid MultipartFile file) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return operationSheetService.batchCreateAllocateOperationSheet(file, requestUser);
    }

    @Operation(summary = "批量新建维修操作记录表 @author 芦苇")
    @PostMapping("/maintainoperationsheet/create")
    @SaCheckPermission("maintainoperationsheet:add")
    public ResponseDTO<String> createMaintainOperationSheet(@RequestPart("file") @Valid MultipartFile file) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return operationSheetService.batchCreateMaintainOperationSheet(file, requestUser);
    }


    @Operation(summary = "批量新建rma操作记录表 @author 芦苇")
    @PostMapping("/rmaoperationsheet/create")
    @SaCheckPermission("rmaoperationsheet:add")
    public ResponseDTO<String> createRmaOperationSheet(@RequestPart("file") @Valid MultipartFile file) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return operationSheetService.batchCreateRmaOperationSheet(file, requestUser);
    }

    @Operation(summary = "批量新建可用旧喷头操作记录表 @author 芦苇")
    @PostMapping("/usableoperationsheet/create")
    @SaCheckPermission("usableoperationsheet:add")
    public ResponseDTO<String> createUsableOperationSheet(@RequestPart("file") @Valid MultipartFile file) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return operationSheetService.batchCreateUsableOperationSheet(file, requestUser);
    }
}
