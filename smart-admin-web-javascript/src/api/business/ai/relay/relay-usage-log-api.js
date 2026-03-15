/**
 * Relay 用量日志 API
 */
import { postRequest } from '/@/lib/axios';

export const relayUsageLogApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/relay/usageLog/queryPage', param);
  },
};
