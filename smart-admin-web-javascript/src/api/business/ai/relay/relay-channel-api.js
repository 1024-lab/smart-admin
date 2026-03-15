/**
 * Relay 渠道管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const relayChannelApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/relay/channel/queryPage', param);
  },
  // 新增
  add: (param) => {
    return postRequest('/relay/channel/add', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/relay/channel/update', param);
  },
  // 删除
  delete: (channelId) => {
    return getRequest(`/relay/channel/delete/${channelId}`);
  },
  // 启用/禁用
  updateDisabled: (channelId, disabledFlag) => {
    return getRequest(`/relay/channel/updateDisabled/${channelId}/${disabledFlag}`);
  },
  // 绑定模型
  bindModels: (param) => {
    return postRequest('/relay/channel/bindModels', param);
  },
  // 查询渠道已绑定的模型ID列表
  getBoundModelIds: (channelId) => {
    return getRequest(`/relay/channel/boundModelIds/${channelId}`);
  },
};
