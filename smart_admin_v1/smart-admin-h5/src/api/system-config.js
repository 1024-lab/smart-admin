// 系统参数API
import {
  postAxios,
  getAxios
} from '@/lib/http';
export const systemConfigApi = {
  // 查询系统参数列表
  getSystemConfigList: (data) => {
    return postAxios('/admin/systemConfig/getListPage', data);
  },
  // 添加系统参数
  addSystemConfig: (data) => {
    return postAxios('/admin/systemConfig/add', data);
  },
  // 更新单条系统参数
  updateSystemConfig: (data) => {
    return postAxios('/admin/systemConfig/update', data);
  },
  // 通过key获取对应的信息
  getConfigListByKey: (key) => {
    return getAxios(`/admin/systemConfig/selectByKey?configKey=${key}`);
  },
  // 根据分组查询所有系统配置
  getListByGroup: (group) => {
    return getAxios(`/admin/systemConfig/getListByGroup?group=${group}`);
  },
  // 获取系统版本信息
  getCodeVersion: () => {
    return getAxios('/admin/codeVersion');
  }
};
