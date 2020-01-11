import { postAxios, getAxios } from '@/lib/http';

export const privilegeApi = {
  // 获取所有请求路径
  getAllUrl: data => {
    return getAxios('/privilege/getAllUrl');
  },
  // 获取全部菜单列表
  getMenuList: data => {
    return postAxios('/privilege/menu/queryAll');
  },
  // 菜单批量保存
  addBatchSaveMenu: data => {
    return postAxios('/privilege/menu/batchSaveMenu', data);
  },
  // 查询菜单功能点
  queryPrivilegeFunctionList: menuKey => {
    return postAxios('/privilege/function/query/' + menuKey);
  },
  // 保存更新功能点
  addOrUpdate: data => {
    return postAxios('/privilege/function/saveOrUpdate', data);
  },
  // 更新角色权限
  getRolePower: data => {
    return postAxios('/privilege/updateRolePrivilege', data);
  },
  // 获取角色可选的功能权限
  getListPrivilegeByRoleId: id => {
    return getAxios('/privilege/listPrivilegeByRoleId/' + id);
  }
};
