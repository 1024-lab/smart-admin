import { postAxios, getAxios } from '@/lib/http';
export const heartBeatApi = {
  // 分页查询所有岗位
  queryHeartBeatRecord: data => {
    return postAxios('/heartBeat/query', data);
  }
};
