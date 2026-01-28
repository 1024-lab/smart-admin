/**
 * 知识库 api 封装
 *
 * @Author:    Mission
 * @Date:      2026-01-28 10:24:07
 * @Copyright  Mission
 */
import { postRequest, getRequest } from '/src/lib/axios';

export const aiKnowledgeApi = {

  /**
   * 分页查询  @author  Mission
   */
  queryPage : (param) => {
    return postRequest('/aiKnowledge/queryPage', param);
  },

  /**
   * 增加  @author  Mission
   */
  add: (param) => {
      return postRequest('/aiKnowledge/add', param);
  },

  /**
   * 修改  @author  Mission
   */
  update: (param) => {
      return postRequest('/aiKnowledge/update', param);
  },


  /**
   * 删除  @author  Mission
   */
  delete: (id) => {
      return getRequest(`/aiKnowledge/delete/${id}`);
  },

  /**
   * 批量删除  @author  Mission
   */
  batchDelete: (idList) => {
      return postRequest('/aiKnowledge/batchDelete', idList);
  },

};
