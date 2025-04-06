package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseEmployeeVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerStockInOperationSheetQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo.SprinklerStockInOperationSheetVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@OperateLog
public class SprinklerController {
    @Resource
    private SprinklerService sprinklerService;

    @Operation(summary = "分页查询喷头模块 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/page/query")
    @SaCheckPermission("sprinklermanager:sprinkler:query")
    public ResponseDTO<PageResult<SprinklerVO>> queryByPage(@RequestBody @Valid SprinklerQueryForm queryForm) {
        return sprinklerService.queryByPage(queryForm);
    }

    @Operation(summary = "批量新建喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/create")
    @SaCheckPermission("sprinklermanager:sprinkler:add")
    public ResponseDTO<String> createSprinkler(
            @RequestPart("file") @Valid MultipartFile file

    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return sprinklerService.batchImport(file, requestUser);
    }

    @Operation(summary = "查询喷头详情 @author 芦苇")
    @GetMapping("/sprinklermanager/sprinkler/get/{sprinklerId}")
    @SaCheckPermission("sprinklermanager:sprinkler:detail")
    public ResponseDTO<SprinklerVO> getDetail(@PathVariable Long sprinklerId) {
        return ResponseDTO.ok(sprinklerService.getDetail(sprinklerId));
    }

    @Operation(summary = "分页查询喷头入库信息 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/stockinoperationsheet/queryPage")
    public ResponseDTO<PageResult<SprinklerStockInOperationSheetVO>> queryPageStockInOperationSheetList(@RequestBody @Valid SprinklerStockInOperationSheetQueryForm queryForm){
        return ResponseDTO.ok(sprinklerService.queryPageStockInOperationSheetList(queryForm));
    }

}
