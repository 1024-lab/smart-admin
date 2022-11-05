import { postAxios, getAxios } from '@/lib/http';
export const employeeApi = {
  // 员工管理查询
  getEmployeeList: (data) => {
    return postAxios('admin/employee/query', data);
  },
  // 添加员工
  addEmployee: (data) => {
    return postAxios('admin/employee/add', data);
  },
  // 更新员工信息
  updateEmployee: (data) => {
    return postAxios('admin/employee/update', data);
  },
  // 禁用启用单个员工
  updateStatus: (employeeId, status) => {
    return getAxios('admin/employee/updateStatus/' + employeeId + '/' + status);
  },
  // 批量禁用
  updateStatusBatch: (data) => {
    return postAxios('admin/employee/batchUpdateStatus', data);
  },
  // 单个员工角色授权
  updateRoles: (data) => {
    return postAxios('admin/employee/updateRoles', data);
  },
  // 修改密码
  updatePwd: (data) => {
    return postAxios('admin/employee/updatePwd', data);
  },
  // 重置密码
  resetPassword: (employeeId) => {
    return getAxios('admin/employee/resetPasswd/' + employeeId);
  },
  // 通过部门id获取当前部门的人员&没有部门的人
  getListEmployeeByDeptId: (departmentId) => {
    return getAxios('admin/employee/listEmployeeByDeptId/' + departmentId);
  },
  // 删除员工
  deleteEmployee: (employeeId) => {
    return postAxios('admin/employee/delete/' + employeeId);
  }
};
