/*
 * 代码生成器
 *
 * @Author:    卓大
 * @Date:      2022-09-03 21:51:54
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { getRequest, postRequest,download } from '/@/lib/axios';

export const codeGeneratorApi = {
  // 查询数据库的表 @author 卓大
  queryTableList: (param) => {
    return postRequest('/support/codeGenerator/table/queryTableList', param);
  },

  // 查询表的列 @author 卓大
  getTableColumns: (table) => {
    return getRequest(`/support/codeGenerator/table/getTableColumns/${table}`);
  },

  // ------------------- 配置 -------------------

  // 获取表的配置信息 @author 卓大
  getConfig: (table) => {
    return getRequest(`/support/codeGenerator/table/getConfig/${table}`);
  },

  // 更新配置信息 @author 卓大
  updateConfig: (param) => {
    return postRequest('/support/codeGenerator/table/updateConfig', param);
  },

  // ------------------- 生成 -------------------

  // 预览代码 @author 卓大
  preview: (param) => {
    return postRequest('/support/codeGenerator/code/preview', param);
  },

  // 下载代码 @author 卓大
  downloadCode: (tableName) => {
    return download(`${tableName}.zip`,`/support/codeGenerator/code/download/${tableName}`);
  },
};
