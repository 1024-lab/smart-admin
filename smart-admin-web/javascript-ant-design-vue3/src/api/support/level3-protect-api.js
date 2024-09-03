/**
 * 三级等保 api 封装
 *
 * @Author:    1024创新实验室-主任-卓大
 * @Date:      2024-07-31 21:02:37
 * @Copyright  1024创新实验室
 */
import { postRequest, getRequest } from '/src/lib/axios';

export const level3ProtectApi = {
  /**
   * 查询 三级等保配置 @author 1024创新实验室-主任-卓大
   */
  getConfig: () => {
    return getRequest('/support/protect/level3protect/getConfig');
  },

  /**
   * 更新三级等保配置 @author 1024创新实验室-主任-卓大
   */
  updateConfig: (form) => {
    return postRequest('/support/protect/level3protect/updateConfig', form);
  },
};
