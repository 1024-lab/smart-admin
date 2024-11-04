/*
 * 单据序列号
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 21:57:52
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const serialNumberApi = {
  // 生成单号 @author 卓大
  generate: (generateForm) => {
    return postRequest('/support/serialNumber/generate', generateForm);
  },
  // 获取所有单号定义 @author 卓大
  getAll: () => {
    return getRequest('/support/serialNumber/all');
  },
  // 获取生成记录 @author 卓大
  queryRecord: (form) => {
    return postRequest('/support/serialNumber/queryRecord', form);
  },
};
