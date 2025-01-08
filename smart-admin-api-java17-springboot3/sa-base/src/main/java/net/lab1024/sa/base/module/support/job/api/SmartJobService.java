package net.lab1024.sa.base.module.support.job.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.job.api.domain.*;
import net.lab1024.sa.base.module.support.job.config.SmartJobAutoConfiguration;
import net.lab1024.sa.base.module.support.job.constant.SmartJobTriggerTypeEnum;
import net.lab1024.sa.base.module.support.job.constant.SmartJobUtil;
import net.lab1024.sa.base.module.support.job.repository.SmartJobDao;
import net.lab1024.sa.base.module.support.job.repository.SmartJobLogDao;
import net.lab1024.sa.base.module.support.job.repository.domain.SmartJobEntity;
import net.lab1024.sa.base.module.support.job.repository.domain.SmartJobLogEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 定时任务 接口业务管理
 * 如果不需要通过接口管理定时任务 可以删除此类
 *
 * @author huke
 * @date 2024/6/17 20:41
 */
@ConditionalOnBean(SmartJobAutoConfiguration.class)
@Service
public class SmartJobService {

    @Resource
    private SmartJobDao jobDao;

    @Resource
    private SmartJobLogDao jobLogDao;

    @Resource
    private SmartJobClientManager jobClientManager;

    /**
     * 查询 定时任务详情
     *
     * @param jobId
     * @return
     */
    public ResponseDTO<SmartJobVO> queryJobInfo(Integer jobId) {
        SmartJobEntity jobEntity = jobDao.selectById(jobId);
        if (null == jobEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        SmartJobVO jobVO = SmartBeanUtil.copy(jobEntity, SmartJobVO.class);
        // 处理设置job详情
        this.handleJobInfo(Lists.newArrayList(jobVO));
        return ResponseDTO.ok(jobVO);
    }

    /**
     * 分页查询 定时任务
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<SmartJobVO>> queryJob(SmartJobQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SmartJobVO> jobList = jobDao.query(page, queryForm);
        PageResult<SmartJobVO> pageResult = SmartPageUtil.convert2PageResult(page, jobList);
        // 处理设置job详情
        this.handleJobInfo(jobList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 处理设置 任务信息
     *
     * @param jobList
     */
    private void handleJobInfo(List<SmartJobVO> jobList) {
        if (CollectionUtils.isEmpty(jobList)) {
            return;
        }
        // 查询最后一次执行记录
        List<Long> logIdList = jobList.stream().map(SmartJobVO::getLastExecuteLogId).filter(Objects::nonNull).collect(Collectors.toList());
        Map<Long, SmartJobLogVO> lastLogMap = Collections.emptyMap();
        if (CollectionUtils.isNotEmpty(logIdList)) {
            lastLogMap = jobLogDao.selectBatchIds(logIdList)
                    .stream()
                    .collect(Collectors.toMap(SmartJobLogEntity::getLogId, e -> SmartBeanUtil.copy(e, SmartJobLogVO.class)));
        }

        // 循环处理任务信息
        for (SmartJobVO jobVO : jobList) {
            // 设置最后一次执行记录
            Long lastExecuteLogId = jobVO.getLastExecuteLogId();
            if (null != lastExecuteLogId) {
                jobVO.setLastJobLog(lastLogMap.get(lastExecuteLogId));
            }
            // 计算未来5次执行时间
            if (jobVO.getEnabledFlag()) {
                List<LocalDateTime> nextTimeList = SmartJobUtil.queryNextTimeFromNow(jobVO.getTriggerType(), jobVO.getTriggerValue(), jobVO.getLastExecuteTime(), 5);
                jobVO.setNextJobExecuteTimeList(nextTimeList);
            }
        }
    }

    /**
     * 分页查询 定时任务-执行记录
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<SmartJobLogVO>> queryJobLog(SmartJobLogQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SmartJobLogVO> jobList = jobLogDao.query(page, queryForm);
        PageResult<SmartJobLogVO> pageResult = SmartPageUtil.convert2PageResult(page, jobList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 添加定时任务
     *
     * @param addForm
     * @return
     */
    public synchronized ResponseDTO<String> addJob(SmartJobAddForm addForm) {
        // 校验参数
        ResponseDTO<String> checkRes = this.checkParam(addForm);
        if (!checkRes.getOk()) {
            return checkRes;
        }

        // 校验重复的执行类
        SmartJobEntity existJobClass = jobDao.selectByJobClass(addForm.getJobClass());
        if (null != existJobClass && !existJobClass.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("已经存在相同的执行类");
        }

        // 添加数据
        SmartJobEntity jobEntity = SmartBeanUtil.copy(addForm, SmartJobEntity.class);
        jobDao.insert(jobEntity);

        // 更新执行端
        SmartJobMsg jobMsg = new SmartJobMsg();
        jobMsg.setJobId(jobEntity.getJobId());
        jobMsg.setMsgType(SmartJobMsg.MsgTypeEnum.UPDATE_JOB);
        jobMsg.setUpdateName(addForm.getUpdateName());
        jobClientManager.publishToClient(jobMsg);
        return ResponseDTO.ok();
    }

    /**
     * 更新定时任务
     *
     * @param updateForm
     * @return
     */
    public synchronized ResponseDTO<String> updateJob(SmartJobUpdateForm updateForm) {
        // 校验参数
        Integer jobId = updateForm.getJobId();
        SmartJobEntity jobEntity = jobDao.selectById(jobId);
        if (null == jobEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        ResponseDTO<String> checkRes = this.checkParam(updateForm);
        if (!checkRes.getOk()) {
            return checkRes;
        }

        // 校验重复的执行类
        SmartJobEntity existJobClass = jobDao.selectByJobClass(updateForm.getJobClass());
        if (null != existJobClass && !existJobClass.getDeletedFlag() && !existJobClass.getJobId().equals(jobId)) {
            return ResponseDTO.userErrorParam("已经存在相同的执行类");
        }

        // 更新数据
        jobEntity = SmartBeanUtil.copy(updateForm, SmartJobEntity.class);
        jobDao.updateById(jobEntity);

        // 更新执行端
        SmartJobMsg jobMsg = new SmartJobMsg();
        jobMsg.setJobId(jobId);
        jobMsg.setMsgType(SmartJobMsg.MsgTypeEnum.UPDATE_JOB);
        jobMsg.setUpdateName(updateForm.getUpdateName());
        jobClientManager.publishToClient(jobMsg);
        return ResponseDTO.ok();
    }

    /**
     * 校验参数
     * 如需其他校验，请自行添加校验逻辑
     *
     * @param addForm
     * @return
     */
    private ResponseDTO<String> checkParam(SmartJobAddForm addForm) {
        // 校验触发时间配置
        String triggerType = addForm.getTriggerType();
        String triggerValue = addForm.getTriggerValue();
        if (SmartJobTriggerTypeEnum.CRON.equalsValue(triggerType) && !SmartJobUtil.checkCron(triggerValue)) {
            return ResponseDTO.userErrorParam("cron表达式错误");
        }
        if (SmartJobTriggerTypeEnum.FIXED_DELAY.equalsValue(triggerType) && !SmartJobUtil.checkFixedDelay(triggerValue)) {
            return ResponseDTO.userErrorParam("固定间隔配置错误：必须是大于0的整数");
        }
        // 校验job class
        return SmartJobUtil.checkJobClass(addForm.getJobClass());
    }

    /**
     * 更新定时任务-是否开启
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> updateJobEnabled(SmartJobEnabledUpdateForm updateForm) {
        Integer jobId = updateForm.getJobId();
        SmartJobEntity jobEntity = jobDao.selectById(jobId);
        if (null == jobEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        Boolean enabledFlag = updateForm.getEnabledFlag();
        if (Objects.equals(enabledFlag, jobEntity.getEnabledFlag())) {
            return ResponseDTO.ok();
        }
        // 更新数据
        jobEntity = new SmartJobEntity();
        jobEntity.setJobId(jobId);
        jobEntity.setEnabledFlag(enabledFlag);
        jobEntity.setUpdateName(updateForm.getUpdateName());
        jobDao.updateById(jobEntity);

        // 更新执行端
        SmartJobMsg jobMsg = new SmartJobMsg();
        jobMsg.setJobId(jobId);
        jobMsg.setMsgType(SmartJobMsg.MsgTypeEnum.UPDATE_JOB);
        jobMsg.setUpdateName(updateForm.getUpdateName());
        jobClientManager.publishToClient(jobMsg);
        return ResponseDTO.ok();
    }

    /**
     * 执行定时任务
     * 忽略任务的开启状态,立即执行一次
     *
     * @param executeForm
     * @return
     */
    public ResponseDTO<String> execute(SmartJobExecuteForm executeForm) {
        Integer jobId = executeForm.getJobId();
        SmartJobEntity jobEntity = jobDao.selectById(jobId);
        if (null == jobEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 更新执行端
        SmartJobMsg jobMsg = new SmartJobMsg();
        jobMsg.setJobId(jobId);
        jobMsg.setParam(executeForm.getParam());
        jobMsg.setMsgType(SmartJobMsg.MsgTypeEnum.EXECUTE_JOB);
        jobMsg.setUpdateName(executeForm.getUpdateName());
        jobClientManager.publishToClient(jobMsg);
        return ResponseDTO.ok();
    }

    /**
     * 移除定时任务
     * 物理删除
     *
     * @return
     * @author huke
     */
    public synchronized ResponseDTO<String> deleteJob(Integer jobId, RequestUser requestUser) {
        // 删除任务
        jobDao.updateDeletedFlag(jobId, Boolean.TRUE);

        // 更新执行端
        SmartJobMsg jobMsg = new SmartJobMsg();
        jobMsg.setJobId(jobId);
        jobMsg.setMsgType(SmartJobMsg.MsgTypeEnum.UPDATE_JOB);
        jobMsg.setUpdateName(requestUser.getUserName());
        jobClientManager.publishToClient(jobMsg);
        return ResponseDTO.ok();
    }

}
