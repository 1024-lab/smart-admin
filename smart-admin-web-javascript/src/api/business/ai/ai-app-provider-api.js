/**
 * 应用提供商 api 封装
 *
 * @Author:    Mission
 * @Date:      2025-12-21 16:06:19
 * @Copyright  Mission
 */
import { postRequest, getRequest } from '/src/lib/axios';

export const aiAppProviderApi = {

  /**
   * 分页查询  @author  Mission
   */
  queryPage : (param) => {
    return postRequest('/aiAppProvider/queryPage', param);
  },

  /**
   * 增加  @author  Mission
   */
  add: (param) => {
      return postRequest('/aiAppProvider/add', param);
  },

  /**
   * 修改  @author  Mission
   */
  update: (param) => {
      return postRequest('/aiAppProvider/update', param);
  },


  /**
   * 删除  @author  Mission
   */
  delete: (id) => {
      return getRequest(`/aiAppProvider/delete/${id}`);
  },

  /**
   * 批量删除  @author  Mission
   */
  batchDelete: (idList) => {
      return postRequest('/aiAppProvider/batchDelete', idList);
  },

  /**
   * 查询所有供应商  @author  Mission
   */
  queryAll: () => {
      return getRequest('/aiAppProvider/queryAll');
  },

};
