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

    @Operation(summary = "批量新建入库操作字段表 @author 芦苇")
    @PostMapping("/stockinoperationsheet/create")
    @SaCheckPermission("stockinoperationsheet:add")
    public ResponseDTO<String> createStockInOperationSheet(@RequestPart("file") @Valid MultipartFile file) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return operationSheetService.batchCreateStockInOperationSheet(file, requestUser);
    }
}
