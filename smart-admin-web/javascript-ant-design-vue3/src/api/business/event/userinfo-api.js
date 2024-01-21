/**
 * 用户表 api 封装
 *
 * @Author:    my
 * @Date:      2024-01-10 15:39:54
 * @Copyright  my
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const userinfoApi = {

  /**
   * 分页查询  @author  my
   */
  queryPage : (param) => {
    return postRequest('/userinfo/queryPage', param);
  },

  /**
   * 增加  @author  my
   */
  add: (param) => {
      return postRequest('/userinfo/add', param);
  },

  /**
   * 修改  @author  my
   */
  update: (param) => {
      return postRequest('/userinfo/update', param);
  },


  /**
   * 删除  @author  my
   */
  delete: (id) => {
      return getRequest(`/userinfo/delete/${id}`);
  },

  /**
   * 批量删除  @author  my
   */
  batchDelete: (idList) => {
      return postRequest('/userinfo/batchDelete', idList);
  },

};
