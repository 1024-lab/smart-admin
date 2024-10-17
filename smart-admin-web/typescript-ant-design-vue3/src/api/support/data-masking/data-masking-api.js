/**
 *  数据脱敏api
 *
 * @Author:    1024创新实验室-主任-卓大
 * @Date:      2024-07-31 21:02:37
 * @Copyright  1024创新实验室
 */
import { getRequest } from '/src/lib/axios';

export const dataMaskingApi = {
  /**
   * 查询脱敏数据
   */
  query: () => {
    return getRequest('/support/dataMasking/demo/query');
  },
};
