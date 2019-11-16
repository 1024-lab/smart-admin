package com.gangquan360.smartadmin.module.position;

import com.gangquan360.smartadmin.common.anno.OperateLog;
import com.gangquan360.smartadmin.common.domain.PageResultDTO;
import com.gangquan360.smartadmin.common.domain.ResponseDTO;
import com.gangquan360.smartadmin.constant.SwaggerTagConst;
import com.gangquan360.smartadmin.module.position.domain.dto.PositionAddDTO;
import com.gangquan360.smartadmin.module.position.domain.dto.PositionQueryDTO;
import com.gangquan360.smartadmin.module.position.domain.dto.PositionResultVO;
import com.gangquan360.smartadmin.module.position.domain.dto.PositionUpdateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author zzr
 */
@Api(tags = {SwaggerTagConst.Admin.MANAGER_JOB})
@OperateLog
@RestController
public class PositionController {

    @Autowired
    private PositionService positionService;

    @ApiOperation(value = "分页查询所有岗位", notes = "分页查询所有岗位 @author zzr")
    @PostMapping("/position/getListPage")
    public ResponseDTO<PageResultDTO<PositionResultVO>> getJobPage(@RequestBody @Valid PositionQueryDTO queryDTO) {
        return positionService.queryPositionByPage(queryDTO);
    }

    @ApiOperation(value = "添加岗位", notes = "添加岗位 @author zzr")
    @PostMapping("/position/add")
    public ResponseDTO<String> addJob(@RequestBody @Valid PositionAddDTO addDTO) {
        return positionService.addPosition(addDTO);
    }

    @ApiOperation(value = "更新岗位", notes = "更新岗位 @author zzr")
    @PostMapping("/position/update")
    public ResponseDTO<String> updateJob(@RequestBody @Valid PositionUpdateDTO updateDTO) {
        return positionService.updatePosition(updateDTO);
    }

    @ApiOperation(value = "根据ID查询岗位", notes = "根据ID查询岗位 @author zzr")
    @GetMapping("/position/queryById/{id}")
    public ResponseDTO<PositionResultVO> queryJobById(@PathVariable Long id) {
        return positionService.queryPositionById(id);
    }

    @ApiOperation(value = "根据ID删除岗位", notes = "根据ID删除岗位 @author zzr")
    @GetMapping("/position/remove/{id}")
    public ResponseDTO<String> removeJob(@PathVariable Long id) {
        return positionService.removePosition(id);
    }

}
