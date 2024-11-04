/*
 * 意见反馈
 *
 * @Author:    1024创新实验室：开云
 * @Date:      2022-09-03 21:56:31
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { postRequest } from '/@/lib/axios';

export const feedbackApi = {
  // 意见反馈-新增
  addFeedback: (params) => {
    return postRequest('/support/feedback/add', params);
  },
  // 意见反馈-分页查询
  queryFeedback: (params) => {
    return postRequest('/support/feedback/query', params);
  },
};
