// 任务调度API
import {
  postAxios,
  getAxios
} from '@/lib/http';
export const onlineUserApi = {
  // 查询在线员工列表
  getOnlineUserList: (data) => {
    return postAxios('/userOnLine/query', data);
  }

};
