/*
 * 操作日志
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 21:56:45
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { postRequest, getRequest } from '/src/lib/axios';

export const operateLogApi = {
  // 分页查询 @author 卓大
  queryList: (param) => {
    return postRequest('/support/operateLog/page/query', param);
  },
  // 详情 @author 卓大
  detail: (id) => {
    return getRequest(`/support/operateLog/detail/${id}`);
  },
  // 分页查询当前登录人信息 @author 善逸
  queryListLogin: (param) => {
    return postRequest('/support/operateLog/page/query/login', param);
  },
};
