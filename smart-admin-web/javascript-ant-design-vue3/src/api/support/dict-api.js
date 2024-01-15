/*
 * 字典
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 21:55:25
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { postRequest, getRequest } from '/src/lib/axios';

export const dictApi = {
  // 分页查询数据字典KEY - @author 卓大
  keyQuery: (param) => {
    return postRequest('/support/dict/key/query', param);
  },
  // 查询全部字典key - @author 卓大
  queryAllKey: () => {
    return getRequest('/support/dict/key/queryAll');
  },
  /**
   * 分页查询数据字典value - @author 卓大  
   */ 
  valueQuery: (param) => {
    return postRequest('/support/dict/value/query', param);
  },
  // 数据字典KEY-添加- @author 卓大
  keyAdd: (param) => {
    return postRequest('/support/dict/key/add', param);
  },
  // 分页查询数据字典value - @author 卓大
  valueAdd: (param) => {
    return postRequest('/support/dict/value/add', param);
  },
  // 数据字典key-更新- @author 卓大
  keyEdit: (param) => {
    return postRequest('/support/dict/key/edit', param);
  },
  // 数据字典Value-更新- @author 卓大
  valueEdit: (param) => {
    return postRequest('/support/dict/value/edit', param);
  },
  // 数据字典key-删除- @author 卓大
  keyDelete: (keyIdList) => {
    return postRequest('/support/dict/key/delete', keyIdList);
  },
  // 数据字典Value-删除- @author 卓大
  valueDelete: (valueIdList) => {
    return postRequest('/support/dict/value/delete', valueIdList);
  },
  // 缓存刷新- @author 卓大
  cacheRefresh: () => {
    return getRequest('/support/dict/cache/refresh');
  },
  // 数据字典-值列表- @author 卓大
  valueList: (keyCode) => {
    return getRequest(`/support/dict/value/list/${keyCode}`);
  },
};
