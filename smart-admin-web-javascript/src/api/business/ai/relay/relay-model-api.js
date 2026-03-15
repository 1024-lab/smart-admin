/**
 * Relay 模型管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const relayModelApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/relay/model/queryPage', param);
  },
  // 新增
  add: (param) => {
    return postRequest('/relay/model/add', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/relay/model/update', param);
  },
  // 删除
  delete: (modelId) => {
    return getRequest(`/relay/model/delete/${modelId}`);
  },
  // 启用/禁用
  updateDisabled: (modelId, disabledFlag) => {
    return getRequest(`/relay/model/updateDisabled/${modelId}/${disabledFlag}`);
  },
};
