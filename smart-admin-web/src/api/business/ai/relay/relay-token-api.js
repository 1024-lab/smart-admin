/**
 * Relay Token管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const relayTokenApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/relay/token/queryPage', param);
  },
  // 新增
  add: (param) => {
    return postRequest('/relay/token/add', param);
  },
  // 修改
  update: (param) => {
    return postRequest('/relay/token/update', param);
  },
  // 删除
  delete: (tokenId) => {
    return getRequest(`/relay/token/delete/${tokenId}`);
  },
};
