/* 
  *  表格列设置
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-26 23:45:51 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  */

import _ from 'lodash';

/**
 * 将原视表格列和用户表格列进行合并、排序
 * @param {*} originalTableColumnArray
 * @param {*} userTableColumnArray
 */
export function mergeColumn(originalTableColumnArray, userTableColumnArray) {
  if (!userTableColumnArray) {
    return originalTableColumnArray;
  }

  //第一步：将用户的列数据转为Map，以后备使用
  let userTableColumnMap = new Map();
  for (const item of userTableColumnArray) {
    userTableColumnMap.set(item.columnKey, item);
  }

  //第二步：以前端的table columns列为基础，将用户后端的数据填充到前端表格列里
  let fontColumnSort = 1;
  let newColumns = [];
  for (const fontColumn of originalTableColumnArray) {
    //原始表格列默认显示
    fontColumn.columnKey = fontColumn.dataIndex;
    fontColumn.showFlag = true;
    fontColumn.sort = fontColumnSort;

    // 如果用户存在此列，则覆盖 sort和width、showFlag字段
    let userColumn = userTableColumnMap.get(fontColumn.columnKey);
    if (userColumn) {
      fontColumn.sort = userColumn.sort;
      fontColumn.showFlag = userColumn.showFlag;
      if (userColumn.width) {
        fontColumn.width = userColumn.width;
      }
    }
    newColumns.push(fontColumn);
    fontColumnSort++;
  }

  //第三步：前端列进行排序
  newColumns = _.sortBy(newColumns, (e) => e.sort);
  return newColumns;
}
