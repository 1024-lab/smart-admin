/*
 * 帮助文档
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 21:56:31
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { postRequest, getRequest } from '/src/lib/axios';

export const helpDocApi = {
  // 【管理】帮助文档-分页查询 @author zhuoda
  query: (param) => {
    return postRequest('/support/helpDoc/query', param);
  },

  //【管理】帮助文档-更新 @author zhuoda
  update: (param) => {
    return postRequest('/support/helpDoc/update', param);
  },

  // 【管理】帮助文档-添加 @author zhuoda
  add: (param) => {
    return postRequest('/support/helpDoc/add', param);
  },

  //【管理】帮助文档-删除 @author zhuoda
  delete: (helpDocId) => {
    return getRequest(`/support/helpDoc/delete/${helpDocId}`);
  },

  //【管理】帮助文档-获取详情 @author zhuoda
  getDetail: (helpDocId) => {
    return getRequest(`/support/helpDoc/getDetail/${helpDocId}`);
  },

  //【管理】帮助文档-根据关联id查询 @author zhuoda
  queryHelpDocByRelationId: (relationId) => {
    return getRequest(`/support/helpDoc/queryHelpDocByRelationId/${relationId}`);
  },

  //----------------------- 用户相关 --------------------------------

  //【用户】帮助文档-查询全部 @author zhuoda
  getAllHelpDocList() {
    return getRequest('/support/helpDoc/user/queryAllHelpDocList');
  },

  //【用户】帮助文档-查询全部 @author zhuoda
  view(helpDocId) {
    return getRequest(`/support/helpDoc/user/view/${helpDocId}`);
  },

  //【用户】帮助文档-查询 查看记录 @author zhuoda
  queryViewRecord(param) {
    return postRequest('/support/helpDoc/user/queryViewRecord', param);
  },
};
