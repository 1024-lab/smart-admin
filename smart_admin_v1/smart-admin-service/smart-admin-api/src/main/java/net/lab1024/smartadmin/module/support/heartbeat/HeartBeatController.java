package net.lab1024.smartadmin.module.support.heartbeat;

import net.lab1024.smartadmin.common.anno.OperateLog;
import net.lab1024.smartadmin.common.domain.PageParamDTO;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.SwaggerTagConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Api(tags = {SwaggerTagConst.Admin.MANAGER_HEART_BEAT})
@OperateLog
@RestController
public class HeartBeatController {

    @Autowired
    private HeartBeatService heartBeatService;

    @PostMapping("/heartBeat/query")
    @ApiOperation("查询心跳记录 @author zhuoda")
    public ResponseDTO<PageResultDTO<HeartBeatRecordVO>> query(@RequestBody @Valid PageParamDTO pageParamDTO){
        return heartBeatService.pageQuery(pageParamDTO);
    }

}
