// smartReloadAPI
import {
  postAxios,
  getAxios
} from '@/lib/http';
export const smartReloadApi = {
  // 查询所有
  getSmartReloadList: () => {
    return getAxios('/smartReload/all');
  },
  // 更新单条数据
  updateSmartReloadData: (data) => {
    return postAxios('/smartReload/update', data);
  },
  // 获取执行结果
  getSmartReloadResult: (tag) => {
    return getAxios(`/smartReload/result/${tag}`);
  }

};
