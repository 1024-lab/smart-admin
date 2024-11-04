package net.lab1024.sa.admin.module.system.support;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.job.api.SmartJobService;
import net.lab1024.sa.base.module.support.job.api.domain.*;
import net.lab1024.sa.base.module.support.job.config.SmartJobAutoConfiguration;
import net.lab1024.sa.base.module.support.repeatsubmit.annoation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 定时任务 管理接口
 *
 * @author huke
 * @date 2024/6/17 20:41
 */
@Tag(name = SwaggerTagConst.Support.JOB)
@RestController
@ConditionalOnBean(SmartJobAutoConfiguration.class)
public class AdminSmartJobController extends SupportBaseController {

    @Autowired
    private SmartJobService jobService;

    @Operation(summary = "定时任务-立即执行 @huke")
    @PostMapping("/job/execute")
    @RepeatSubmit
    public ResponseDTO<String> execute(@RequestBody @Valid SmartJobExecuteForm executeForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        executeForm.setUpdateName(requestUser.getUserName());
        return jobService.execute(executeForm);
    }

    @Operation(summary = "定时任务-查询详情 @huke")
    @GetMapping("/job/{jobId}")
    public ResponseDTO<SmartJobVO> queryJobInfo(@PathVariable Integer jobId) {
        return jobService.queryJobInfo(jobId);
    }

    @Operation(summary = "定时任务-分页查询 @huke")
    @PostMapping("/job/query")
    public ResponseDTO<PageResult<SmartJobVO>> queryJob(@RequestBody @Valid SmartJobQueryForm queryForm) {
        return jobService.queryJob(queryForm);
    }

    @Operation(summary = "定时任务-更新-任务信息 @huke")
    @PostMapping("/job/update")
    @RepeatSubmit
    public ResponseDTO<String> updateJob(@RequestBody @Valid SmartJobUpdateForm updateForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        updateForm.setUpdateName(requestUser.getUserName());
        return jobService.updateJob(updateForm);
    }

    @Operation(summary = "定时任务-更新-开启状态 @huke")
    @PostMapping("/job/update/enabled")
    @RepeatSubmit
    public ResponseDTO<String> updateJobEnabled(@RequestBody @Valid SmartJobEnabledUpdateForm updateForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        updateForm.setUpdateName(requestUser.getUserName());
        return jobService.updateJobEnabled(updateForm);
    }

    @Operation(summary = "定时任务-执行记录-分页查询 @huke")
    @PostMapping("/job/log/query")
    public ResponseDTO<PageResult<SmartJobLogVO>> queryJobLog(@RequestBody @Valid SmartJobLogQueryForm queryForm) {
        return jobService.queryJobLog(queryForm);
    }
}