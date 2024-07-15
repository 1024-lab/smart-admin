/*
 * 部门
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 21:58:50
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { getRequest, postRequest } from '/@/lib/axios';

export const departmentApi = {
  /**
   * @description: 查询部门列表 @author 卓大
   * @param {*}
   * @return {*}
   */
  queryAllDepartment: () => {
    return getRequest('/department/listAll');
  },

  /**
   * @description: 查询部门树形列表 @author 卓大
   * @param {*}
   * @return {*}
   */
   queryDepartmentTree: () => {
    return getRequest('/department/treeList');
  },

  /**
   * @description: 添加部门 @author 卓大
   * @param {*}
   * @return {*}
   */
  addDepartment: (param) => {
    return postRequest('/department/add', param);
  },
  /**
   * @description: 更新部门信息 @author 卓大
   * @param {*}
   * @return {*}
   */
  updateDepartment: (param) => {
    return postRequest('/department/update', param);
  },
  /**
   * @description: 获取校区列表 @author 卓大
   * @param {*}
   * @return {*}
   */
  deleteDepartment: (departmentId) => {
    return getRequest(`/department/delete/${departmentId}`);
  },
};
