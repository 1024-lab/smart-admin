/**
 * 系统更新日志 api 封装
 *
 * @Author:    卓大
 * @Date:      2022-09-26 14:53:50
 * @Copyright  1024创新实验室
 */
import { postRequest, getRequest } from '@/lib/smart-request';

export const changeLogApi = {
  /**
   * 分页查询  @author  卓大
   */
  queryPage: (param) => {
    return postRequest('/support/changeLog/queryPage', param);
  },

  /**
   * 详情  @author  卓大
   */
  getDetail: (changeLogId) => {
    return getRequest(`/support/changeLog/getDetail/${changeLogId}`);
  },
};
