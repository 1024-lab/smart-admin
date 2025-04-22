package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerStockInQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerStockInExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerStockInVO;
import net.lab1024.sa.admin.util.AdminRequestUtil;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartDateFormatterEnum;
import net.lab1024.sa.base.common.util.SmartExcelUtil;
import net.lab1024.sa.base.common.util.SmartLocalDateUtil;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.common.util.SmartResponseUtil;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@OperateLog
@Tag(name = AdminSwaggerTagConst.System.SYSTEM_DEPARTMENT)
public class SprinklerController {
    @Resource
    private SprinklerService sprinklerService;

    @Operation(summary = "分页查询喷头模块 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/page/query")
    @SaCheckPermission("sprinklermanager:sprinkler:query")
    public ResponseDTO<PageResult<SprinklerStockInVO>> queryByPage(@RequestBody @Valid SprinklerStockInQueryForm queryForm) {
        return sprinklerService.queryByPage(queryForm);
    }


    @Operation(summary = "批量新建所有喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/create")
    @SaCheckPermission("sprinklermanager:sprinkler:add")
    public ResponseDTO<String> createSprinkler(
            @RequestPart("file") @Valid MultipartFile file
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return sprinklerService.batchSprinklerCreate(file, requestUser);
    }

    @Operation(summary = "批量新建喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/repositorysprinkler/create")
    @SaCheckPermission("sprinklermanager:repositorysprinkler:add")
    public ResponseDTO<String> createRepositorySprinkler(
//            @RequestPart("file") @Valid MultipartFile file,
//            @RequestPart("type") @Valid Integer type
            MultipartFile file,
            Integer type
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return sprinklerService.batchRepositorySprinklerCreate(file, requestUser, type);
    }


    @Operation(summary = "查询喷头详情 @author 芦苇")
    @GetMapping("/sprinklermanager/sprinkler/get/{sprinklerId}")
    @SaCheckPermission("sprinklermanager:sprinkler:detail")
    public ResponseDTO<SprinklerStockInVO> getDetail(@PathVariable Long sprinklerId) {
        return ResponseDTO.ok(sprinklerService.getDetail(sprinklerId));
    }


    @Operation(summary = "导出喷头信息 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/exportExcel")
    public void exportExcel(@RequestBody @Valid SprinklerStockInQueryForm queryForm, HttpServletResponse response) throws IOException {
        List<SprinklerStockInExcelVO> data = sprinklerService.getExcelExportData(queryForm);
        if (CollectionUtils.isEmpty(data)) {
            SmartResponseUtil.write(response, ResponseDTO.userErrorParam("暂无数据"));
            return;
        }

        String watermark = AdminRequestUtil.getRequestUser().getActualName();
        watermark += SmartLocalDateUtil.format(LocalDateTime.now(), SmartDateFormatterEnum.YMD_HMS);

        SmartExcelUtil.exportExcelWithWatermark(response,"喷头基本信息.xlsx","喷头信息", SprinklerStockInExcelVO.class,data,watermark);

    }

}
