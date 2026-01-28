/**
 * 模型配置表 api 封装
 *
 * @Author:    Mission
 * @Date:      2026-01-28 09:59:38
 * @Copyright  Mission
 */
import { postRequest, getRequest } from '/src/lib/axios';

export const aiModelConfigApi = {

  /**
   * 分页查询  @author  Mission
   */
  queryPage : (param) => {
    return postRequest('/aiModelConfig/queryPage', param);
  },

  /**
   * 增加  @author  Mission
   */
  add: (param) => {
      return postRequest('/aiModelConfig/add', param);
  },

  /**
   * 修改  @author  Mission
   */
  update: (param) => {
      return postRequest('/aiModelConfig/update', param);
  },


  /**
   * 删除  @author  Mission
   */
  delete: (id) => {
      return getRequest(`/aiModelConfig/delete/${id}`);
  },

  /**
   * 批量删除  @author  Mission
   */
  batchDelete: (idList) => {
      return postRequest('/aiModelConfig/batchDelete', idList);
  },

};
