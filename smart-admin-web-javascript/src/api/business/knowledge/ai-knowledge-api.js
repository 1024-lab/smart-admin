/**
 * 知识库 api 封装
 *
 * @Author:    Mission
 * @Date:      2026-01-28 10:24:07
 * @Copyright  Mission
 */
import { postRequest, getRequest } from '/src/lib/axios';

export const aiKnowledgeApi = {

  /**
   * 分页查询  @author  Mission
   */
  queryPage : (param) => {
    return postRequest('/aiKnowledge/queryPage', param);
  },

  /**
   * 增加  @author  Mission
   */
  add: (param) => {
      return postRequest('/aiKnowledge/add', param);
  },

  /**
   * 修改  @author  Mission
   */
  update: (param) => {
      return postRequest('/aiKnowledge/update', param);
  },


  /**
   * 删除  @author  Mission
   */
  delete: (id) => {
      return getRequest(`/aiKnowledge/delete/${id}`);
  },

  /**
   * 批量删除  @author  Mission
   */
  batchDelete: (idList) => {
      return postRequest('/aiKnowledge/batchDelete', idList);
  },

};

export const aiDocumentsApi = {

  /**
   * 分页查询文档  @author  Mission
   */
  queryPage : (param) => {
    return postRequest('/aiDocuments/queryPage', param);
  },

  /**
   * 上传文档  @author  Mission
   */
  upload: (param) => {
      return postRequest('/aiDocuments/upload', param);
  },

  /**
   * 删除文档  @author  Mission
   */
  delete: (id) => {
      return getRequest(`/aiDocuments/delete/${id}`);
  },

  /**
   * 批量删除文档  @author  Mission
   */
  batchDelete: (idList) => {
      return postRequest('/aiDocuments/batchDelete', idList);
  },

};

export const aiDocumentSegmentsApi = {

  /**
   * 分页查询文档分片  @author  Mission
   */
  queryPage : (param) => {
    return postRequest('/aiDocumentSegments/queryPage', param);
  },

  /**
   * 删除文档分片  @author  Mission
   */
  delete: (id) => {
      return getRequest(`/aiDocumentSegments/delete/${id}`);
  },

  /**
   * 批量删除文档分片  @author  Mission
   */
  batchDelete: (idList) => {
      return postRequest('/aiDocumentSegments/batchDelete', idList);
  },

};
