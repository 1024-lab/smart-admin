/*
 * 企业信息
 *
 * @Author:    开云
 * @Date:      2022-09-03 21:47:28
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const enterpriseApi = {
  // 新建企业 @author 开云
  create: (param) => {
    return postRequest('/oa/enterprise/create', param);
  },

  // 删除企业 @author 开云
  delete: (enterpriseId) => {
    return getRequest(`/oa/enterprise/delete/${enterpriseId}`);
  },

  // 查询企业详情 @author 开云
  detail: (enterpriseId) => {
    return getRequest(`/oa/enterprise/get/${enterpriseId}`);
  },

  // 分页查询企业模块 @author 开云
  pageQuery: (param) => {
    return postRequest('/oa/enterprise/page/query', param);
  },

  //企业列表查询 含数据范围 @author 开云
  queryList: (type) => {
    let query = '';
    if (type) {
      query = `?type=${type}`;
    }
    return getRequest(`/oa/enterprise/query/list${query}`);
  },

  // 编辑企业 @author 开云
  update: (param) => {
    return postRequest('/oa/enterprise/update', param);
  },
  // 企业全部员工List @author yandy
  employeeList: (param) => {
    return postRequest('/oa/enterprise/employee/list', param);
  },
  // 分页查询企业员工List @author 卓大
  queryPageEmployeeList: (param) => {
    return postRequest('/oa/enterprise/employee/queryPage', param);
  },
  // 添加员工 @author yandy
  addEmployee: (param) => {
    return postRequest('/oa/enterprise/employee/add', param);
  },

  // 删除员工 @author yandy
  deleteEmployee: (param) => {
    return postRequest('/oa/enterprise/employee/delete', param);
  },

};
