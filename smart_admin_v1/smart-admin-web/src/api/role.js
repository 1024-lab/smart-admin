import { postAxios, getAxios } from '@/lib/http';
export const roleApi = {
  // 添加角色
  addRole: (remark, roleName) => {
    const data = {
      remark: remark,
      roleName: roleName
    };
    return postAxios('/role/add', data);
  },
  // 删除角色
  deleteRole: id => {
    return getAxios('/role/delete/' + id);
  },
  // 修改角色
  updateRole: (id, remark, roleName) => {
    const data = {
      id: id,
      remark: remark,
      roleName: roleName
    };
    return postAxios('/role/update', data);
  },
  // 获取角色数据
  getRoleDetail: id => {
    return getAxios('/role/get/' + id);
  },
  // 加载角色列表
  getAllRole: () => {
    return getAxios('role/getAll');
  },
  // 根据角色名字获取对应成员列表
  getListEmployee: data => {
    return postAxios('/role/listEmployee', data);
  },
  // 根据角色id获取角色成员-员工列表
  getAllListEmployee: id => {
    return getAxios('/role/listAllEmployee/' + id);
  },
  // 从角色成员列表中移除员工
  deleteEmployeeRole: param => {
    return getAxios('/role/removeEmployee?employeeId=' + param.employeeId + '&roleId=' + param.roleId);
  },
  // 从角色成员列表中批量移除员工
  deleteEmployeeList: data => {
    return postAxios('/role/removeEmployeeList', data);
  },

  // 添加角色成员方法
  addEmployeeListRole: data => {
    return postAxios('/role/addEmployeeList', data);
  },
  // 通过员工id获取所有角色以及员工具有的角色
  getRoles: id => {
    return getAxios('/role/getRoles/' + id);
  },

};
