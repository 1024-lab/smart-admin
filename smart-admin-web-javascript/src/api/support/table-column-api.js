/*
 * @Description:表格自定义列
 * @version:
 * @Author: zhuoda
 * @Date: 2022-08-17 23:32:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-21
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const tableColumnApi = {
  // 修改表格列 @author zhuoda
  updateTableColumn: (param) => {
    return postRequest('/support/tableColumn/update', param);
  },

  // 查询表格列 @author zhuoda
  getColumns: (tableId) => {
    return getRequest(`/support/tableColumn/getColumns/${tableId}`);
  },

  // 删除表格列 @author zhuoda
  deleteColumns: (tableId) => {
    return getRequest(`/support/tableColumn/delete/${tableId}`);
  },
};
