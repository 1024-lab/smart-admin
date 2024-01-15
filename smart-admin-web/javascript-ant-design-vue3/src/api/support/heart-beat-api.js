/*
 * 心跳
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 21:55:47
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { postRequest } from '/src/lib/axios';

export const heartBeatApi = {
  // 分页查询 @author 卓大
  queryList: (param) => {
    return postRequest('/support/heartBeat/query', param);
  },
};
