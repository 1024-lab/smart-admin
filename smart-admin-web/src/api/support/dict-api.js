/*
 * 字典
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 21:55:25
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const dictApi = {

  // 获取所有字典code @author 1024创新实验室-主任-卓大
  getAllDict: () => {
    return getRequest('/support/dict/getAllDict');
  },

  // 获取全部字典数据 @author 1024创新实验室-主任-卓大
  getAllDictData: () => {
    return getRequest('/support/dict/getAllDictData');
  },

  // 分页查询 @author 1024创新实验室-主任-卓大
  queryDict: (param) => {
    return postRequest('/support/dict/queryPage', param);
  },

  // 添加 @author 1024创新实验室-主任-卓大
  addDict: (param) => {
    return postRequest('/support/dict/add', param);
  },

  // 更新 @author 1024创新实验室-主任-卓大
  updateDict: (param) => {
    return postRequest('/support/dict/update', param);
  },

  // 字典-删除- @author 卓大
  batchDeleteDict: (dictIdList) => {
    return postRequest('/support/dict/batchDelete', dictIdList);
  },

  // 字典 启用 禁用 @author 1024创新实验室-主任-卓大
  updateDisabled: (dictId) => {
    return getRequest(`/support/dict/updateDisabled/${dictId}`);
  },

  // ------------- 查询字典数据 -------------

  // 字典数据 分页查询 @author 1024创新实验室-主任-卓大
  queryDictData: (dictId) => {
    return getRequest(`/support/dict/dictData/queryDictData/${dictId}`);
  },

  // 字典数据 添加 - @author 卓大
  addDictData: (param) => {
    return postRequest('/support/dict/dictData/add', param);
  },

  // 字典数据 更新- @author 卓大
  updateDictData: (param) => {
    return postRequest('/support/dict/dictData/update', param);
  },

  // 字典数据-删除- @author 卓大
  batchDeleteDictData: (dictDataIdList) => {
    return postRequest('/support/dict/dictData/batchDelete', dictDataIdList);
  },

  // 字典数据 启用 禁用 @author 1024创新实验室-主任-卓大
  updateDictDataDisabled: (dictDataId) => {
    return getRequest(`/support/dict/dictData/updateDisabled/${dictDataId}`);
  },
};
