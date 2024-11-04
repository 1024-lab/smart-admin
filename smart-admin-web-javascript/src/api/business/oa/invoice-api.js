/* 
  * OA发票信息
  * 
  * @Author:    善逸 
  * @Date:      2022-09-03 21:48:54 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  */
import { postRequest, getRequest } from '/@/lib/axios';

export const invoiceApi = {

  // 新建发票信息 @author 善逸
  create: (param) => {
    return postRequest('/oa/invoice/create', param);
  },

  // 删除发票信息 @author 善逸
  delete: (bankId) => {
    return getRequest(`/oa/invoice/delete/${bankId}`);
  },

  // 查询发票信息详情 @author 善逸
  detail: (bankId) => {
    return getRequest(`//oa/invoice/get/${bankId}`);
  },

  // 分页查询发票信息 @author 善逸
  pageQuery: (param) => {
    return postRequest('/oa/invoice/page/query', param);
  },

  // 编辑发票信息 @author 善逸
  update: (param) => {
    return postRequest('/oa/invoice/update', param);
  },

  // 查询发票列表 @author 善逸
  queryList: (enterpriseId) => {
    return getRequest(`/oa/invoice/query/list/${enterpriseId}`);
  },

};
