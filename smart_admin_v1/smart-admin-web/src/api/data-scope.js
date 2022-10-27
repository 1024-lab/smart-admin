import { postAxios, getAxios } from '@/lib/http';
export const dataScopeApi = {
  // 批量设置某角色数据范围
  updateDataScope: data => {
    return postAxios('/dataScope/batchSet', data);
  },
  // 数据权限列表
  getDataScopeList: () => {
    return getAxios('/dataScope/list');
  },
  // 获取某角色所设置的数据范围
  getDataScopeByRoleId: roleId => {
    return getAxios('/dataScope/listByRole/' + roleId);
  }
};
