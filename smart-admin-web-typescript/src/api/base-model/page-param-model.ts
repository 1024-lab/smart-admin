/*
 * @Description: 分页查询你参数
 * @Author: zhuoda
 * @Date: 2021-08-12
 * @LastEditTime: 2021-08-14
 * @LastEditors: zhuoda
 */
import { SortItemModel } from './sort-item-model';

/**
 *
 * @export
 * @interface PageResultDto
 */
export interface PageParamModel {
  /**
   * 页码(不能为空)
   * @type {number}
   * @memberof ClueUserQueryForm
   */
  pageNum: number;
  /**
   * 每页数量(不能为空)
   * @type {number}
   * @memberof ClueUserQueryForm
   */
  pageSize: number;

  /**
   * 排序字段集合
   * @type {Array<SortItemDto>}
   * @memberof ClueUserQueryForm
   */
  sortItemList?: Array<SortItemModel>;
}
