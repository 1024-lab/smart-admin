/*
 * 银行卡
 *
 * @Author:    善逸
 * @Date:      2022-09-03 21:42:08
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const bankApi = {
  // 新建银行信息 @author 善逸
  create: (param) => {
    return postRequest('/oa/bank/create', param);
  },

  // 删除银行信息 @author 善逸
  delete: (bankId) => {
    return getRequest(`/oa/bank/delete/${bankId}`);
  },

  // 查询银行信息详情 @author 善逸
  detail: (bankId) => {
    return getRequest(`/oa/bank/get/${bankId}`);
  },

  // 分页查询银行信息 @author 善逸
  pageQuery: (param) => {
    return postRequest('/oa/bank/page/query', param);
  },

  // 编辑银行信息 @author 善逸
  update: (param) => {
    return postRequest('/oa/bank/update', param);
  },

  // 根据企业ID查询不分页的银行列表 @author 善逸
  queryList: (enterpriseId) => {
    return getRequest(`/oa/bank/query/list/${enterpriseId}`);
  },
};
