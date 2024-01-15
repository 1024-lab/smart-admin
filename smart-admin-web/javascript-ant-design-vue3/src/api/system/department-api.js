/*
 * 部门
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 21:58:50
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { getRequest, postRequest } from '/src/lib/axios';

export const departmentApi = {
  /**
   * 查询部门列表 @author 卓大
   */
  queryAllDepartment: () => {
    return getRequest('/department/listAll');
  },

  /**
   * 查询部门树形列表 @author 卓大
   */
  queryDepartmentTree: () => {
    return getRequest('/department/treeList');
  },

  /**
   * 添加部门 @author 卓大
   */
  addDepartment: (param) => {
    return postRequest('/department/add', param);
  },
  /**
   * 更新部门信息 @author 卓大
   */
  updateDepartment: (param) => {
    return postRequest('/department/update', param);
  },
  /**
   * 删除
   */
  deleteDepartment: (departmentId) => {
    return getRequest(`/department/delete/${departmentId}`);
  },
};
