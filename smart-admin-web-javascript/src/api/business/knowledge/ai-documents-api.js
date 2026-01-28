/**
 * 文档列表 api 封装
 *
 * @Author:    Mission
 * @Date:      2026-01-28 20:36:21
 * @Copyright  Mission
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const aiDocumentsApi = {

  /**
   * 分页查询  @author  Mission
   */
  queryPage : (param) => {
    return postRequest('/aiDocuments/queryPage', param);
  },

  /**
   * 增加  @author  Mission
   */
  add: (param) => {
      return postRequest('/aiDocuments/add', param);
  },

  /**
   * 修改  @author  Mission
   */
  update: (param) => {
      return postRequest('/aiDocuments/update', param);
  },


  /**
   * 删除  @author  Mission
   */
  delete: (id) => {
      return getRequest(`/aiDocuments/delete/${id}`);
  },

  /**
   * 批量删除  @author  Mission
   */
  batchDelete: (idList) => {
      return postRequest('/aiDocuments/batchDelete', idList);
  },

};
