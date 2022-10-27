/*
 *  员工
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 21:59:15
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */

import { getRequest, postRequest } from '/@/lib/axios';

export const employeeApi = {
  /**
   * @description: 查询所有员工 @author 卓大
   * @param {*}
   * @return {*}
   */
  queryAll: () => {
    return getRequest('/employee/queryAll');
  },
  /**
   * @description: 员工管理查询
   * @param {*}
   * @return {*}
   */
  queryEmployee: (params) => {
    return postRequest('/employee/query', params);
  },
  /**
   * @description: 添加员工
   * @param {EmployeeAddDto} params
   * @return {*}
   */
  addEmployee: (params) => {
    return postRequest('/employee/add', params);
  },
  /**
   * @description: 更新员工信息
   * @param {EmployeeUpdateDto} params
   * @return {*}
   */
  updateEmployee: (params) => {
    return postRequest('/employee/update', params);
  },
  /**
   * @description: 删除员工
   * @param {number} employeeId
   * @return {*}
   */
  deleteEmployee: (employeeId) => {
    return getRequest(`/employee/delete/${employeeId}`);
  },
  /**
   * @description: 批量删除员工
   * @param {number} employeeIdList
   * @return {*}
   */
  batchDeleteEmployee: (employeeIdList) => {
    return postRequest('/employee/update/batch/delete', employeeIdList);
  },
  /**
   * @description: 批量调整员工部门
   * @return {*}
   */
  batchUpdateDepartmentEmployee: (updateParam) => {
    return postRequest('/employee/update/batch/department', updateParam);
  },
  /**
   * @description: 重置员工密码
   * @param {number} employeeId
   * @return {*}
   */
  resetPassword: (employeeId) => {
    return getRequest(`/employee/update/password/reset/${employeeId}`);
  },
  /**
   * @description: 修改面面
   * @param {number} employeeId
   * @return {*}
   */
  updateEmployeePassword: (param) => {
    return postRequest('/employee/update/password',param);
  },
  /**
   * @description: 更新员工禁用状态
   * @param {number} employeeId
   * @return {*}
   */
  updateDisabled: (employeeId) => {
    return getRequest(`/employee/update/disabled/${employeeId}`);
  },

  // 查询员工-根据部门id
  queryEmployeeByDeptId: (departmentId) => {
    return getRequest(`/employee/query/dept/${departmentId}`);
  },
};
