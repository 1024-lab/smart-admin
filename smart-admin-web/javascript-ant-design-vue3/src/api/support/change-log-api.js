/**
 * 系统更新日志 api 封装
 *
 * @Author:    卓大
 * @Date:      2022-09-26 14:53:50
 * @Copyright  1024创新实验室
 */
import { postRequest, getRequest } from '/src/lib/axios';

export const changeLogApi = {
  /**
   * 分页查询  @author  卓大
   */
  queryPage: (param) => {
    return postRequest('/support/changeLog/queryPage', param);
  },

  /**
   * 增加  @author  卓大
   */
  add: (param) => {
    return postRequest('/support/changeLog/add', param);
  },

  /**
   * 修改  @author  卓大
   */
  update: (param) => {
    return postRequest('/support/changeLog/update', param);
  },

  /**
   * 删除  @author  卓大
   */
  delete: (id) => {
    return getRequest(`/support/changeLog/delete/${id}`);
  },

  /**
   * 批量删除  @author  卓大
   */
  batchDelete: (idList) => {
    return postRequest('/support/changeLog/batchDelete', idList);
  },
};
