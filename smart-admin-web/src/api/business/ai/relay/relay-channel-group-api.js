/**
 * Relay 渠道分组管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const relayChannelGroupApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/relay/channelGroup/queryPage', param);
  },
  // 查询列表（不分页，用于下拉选择）
  queryList: (param) => {
    return postRequest('/relay/channelGroup/queryList', param);
  },
  // 新增
  add: (param) => {
    return postRequest('/relay/channelGroup/add', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/relay/channelGroup/update', param);
  },
  // 删除
  delete: (channelGroupId) => {
    return getRequest(`/relay/channelGroup/delete/${channelGroupId}`);
  },
  // 启用/禁用
  updateDisabled: (channelGroupId, disabledFlag) => {
    return getRequest(`/relay/channelGroup/updateDisabled/${channelGroupId}/${disabledFlag}`);
  },
};
