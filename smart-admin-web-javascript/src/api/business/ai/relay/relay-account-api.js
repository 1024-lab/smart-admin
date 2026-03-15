/**
 * Relay 用户账户 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const relayAccountApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/relay/account/queryPage', param);
  },
  // 新增账户
  add: (param) => {
    return postRequest('/relay/account/add', param);
  },
  // 充值
  recharge: (param) => {
    return postRequest('/relay/account/recharge', param);
  },
  // 启用/禁用
  updateDisabled: (employeeId, disabledFlag) => {
    return getRequest(`/relay/account/updateDisabled/${employeeId}/${disabledFlag}`);
  },
  // 查询额度流水
  queryLedger: (employeeId, pageNum, pageSize) => {
    return getRequest(`/relay/account/ledger/${employeeId}?pageNum=${pageNum}&pageSize=${pageSize}`);
  },
  // 修改账户
  update: (param) => {
    return postRequest('/relay/account/update', param);
  },
  // 删除账户
  delete: (employeeId) => {
    return getRequest(`/relay/account/delete/${employeeId}`);
  },
};
