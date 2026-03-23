import { getRequest, postRequest } from '/@/lib/axios';

export const aiMerchTaskApi = {
  create: (param) => {
    return postRequest('/ai/merch/task/create', param);
  },
  generateImage: (taskId) => {
    return postRequest(`/ai/merch/task/generateImage/${taskId}`);
  },
  generateVideo: (taskId) => {
    return postRequest(`/ai/merch/task/generateVideo/${taskId}`);
  },
  detail: (taskId) => {
    return getRequest(`/ai/merch/task/detail/${taskId}`);
  },
  queryPage: (param) => {
    return postRequest('/ai/merch/task/queryPage', param);
  },
};
