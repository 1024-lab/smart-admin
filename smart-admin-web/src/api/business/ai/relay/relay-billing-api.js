/**
 * Relay 计费会话 API
 */
import { postRequest } from '/@/lib/axios';

export const relayBillingApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/relay/billing/queryPage', param);
  },
};
