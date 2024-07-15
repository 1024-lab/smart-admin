/**
 * 职务表 api 封装
 *
 * @Author:    kaiyun
 * @Date:      2024-06-23 23:31:38
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const positionApi = {

  /**
   * 分页查询  @author  kaiyun
   */
  queryPage : (param) => {
    return postRequest('/position/queryPage', param);
  },

  /**
   * 增加  @author  kaiyun
   */
  add: (param) => {
      return postRequest('/position/add', param);
  },

  /**
   * 修改  @author  kaiyun
   */
  update: (param) => {
      return postRequest('/position/update', param);
  },


  /**
   * 删除  @author  kaiyun
   */
  delete: (id) => {
      return getRequest(`/position/delete/${id}`);
  },

  /**
   * 批量删除  @author  kaiyun
   */
  batchDelete: (idList) => {
    return postRequest('/position/batchDelete', idList);
  },

  /**
   * 查询列表  @author  kaiyun
   */
  queryList: () => {
    return getRequest('/position/queryList');
  },

};
