/**
 * ai应用管理表 api 封装
 *
 * @Author:    Mission
 * @Date:      2025-12-21 16:00:44
 * @Copyright  Mission
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const aiAppApi = {

  /**
   * 分页查询  @author  Mission
   */
  queryPage : (param) => {
    return postRequest('/aiApp/queryPage', param);
  },

  /**
   * 增加  @author  Mission
   */
  add: (param) => {
      return postRequest('/aiApp/add', param);
  },

  /**
   * 修改  @author  Mission
   */
  update: (param) => {
      return postRequest('/aiApp/update', param);
  },


  /**
   * 删除  @author  Mission
   */
  delete: (id) => {
      return getRequest(`/aiApp/delete/${id}`);
  },

  /**
   * 批量删除  @author  Mission
   */
  batchDelete: (idList) => {
      return postRequest('/aiApp/batchDelete', idList);
  },

};
