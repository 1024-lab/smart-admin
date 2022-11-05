import { postAxios, getAxios } from '@/lib/http';
export const departmentApi = {
  // 加载所有的部门子部门
  getLoadDepartment: () => {
    return getAxios('/department/list');
  },
  // 查询部门及员工列表
  getDepartmentEmployeeList: () => {
    return getAxios('/department/listEmployee');
  },
  // 添加部门
  addDepartment: (data) => {
    return postAxios('/department/add', data);
  },
  // 编辑部门
  updateDepartment: (data) => {
    return postAxios('/department/update', data);
  },
  // 删除部门
  deleteDepartment: (data) => {
    return postAxios('/department/delete/' + data);
  },
  // 根据id获取部门信息
  getDepartmentById: (data) => {
    return getAxios('/department/query/' + data);
  },
  // 查询部门列表
  getDepartmentAll: () => {
    return getAxios('/department/listAll');
  },
  // 根据部门名称获取员工列表
  getListEmployeeByDepartmentName: (departmentName) => {
    return getAxios('/department/listEmployeeByDepartmentName?departmentName=' + departmentName);
  },
  // 上下移动
  upOrDown: (departmentId, swapId) => {
    return getAxios('/department/upOrDown/' + departmentId + '/' + swapId);
  },
  // 升级
  upGrade: (departmentId) => {
    return getAxios('/department/upgrade/' + departmentId);
  },
  // 降级
  downGrade: (departmentId, preId) => {
    return getAxios('/department/downgrade/' + departmentId + '/' + preId);
  }
};
