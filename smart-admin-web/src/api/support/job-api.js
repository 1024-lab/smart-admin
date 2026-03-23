/*
 * job api
 *
 * @Author:    huke
 * @Date:      2024/06/25
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const jobApi = {
  // 分页查询 @huke
  queryJob: (param) => {
    return postRequest('/support/job/query', param);
  },
  // 定时任务-查询详情 @huke
  queryJobInfo: (param) => {
    return getRequest(`/support/job/${param}`);
  },
  // 执行任务 @huke
  executeJob: (param) => {
    return postRequest('/support/job/execute', param);
  },
  // 定时任务-新增-任务信息 @huke
  addJob: (param) => {
    return postRequest('/support/job/add', param);
  },
  // 定时任务-更新-任务信息 @huke
  updateJob: (param) => {
    return postRequest('/support/job/update', param);
  },
  // 定时任务-更新-开启状态 @huke
  updateJobEnabled: (param) => {
    return postRequest('/support/job/update/enabled', param);
  },
  // 定时任务-执行记录-分页查询 @huke
  queryJobLog: (param) => {
    return postRequest('/support/job/log/query', param);
  },
  // 定时任务-删除  @zhuoda
  deleteJob: (param) => {
    return getRequest(`/support/job/delete?jobId=${param}`);
  },
};
