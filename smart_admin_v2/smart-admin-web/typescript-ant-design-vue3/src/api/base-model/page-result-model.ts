/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-11 18:12:07
 * @LastEditTime: 2021-08-12 15:11:54
 * @LastEditors: zhuoda
 */
/**
 *
 * @export
 * @interface PageResultModel
 */
export interface PageResultModel<T> {
  /**
   * 是否为空
   * @type {boolean}
   * @memberof PageResultModel
   */
  emptyFlag?: boolean;
  /**
   * 结果集
   * @type {Array<T>}
   * @memberof PageResultModel
   */
  list: Array<T>;
  /**
   * 当前页
   * @type {number}
   * @memberof PageResultModel
   */
  pageNum?: number;
  /**
   * 每页的数量
   * @type {number}
   * @memberof PageResultModel
   */
  pageSize?: number;
  /**
   * 总页数
   * @type {number}
   * @memberof PageResultModel
   */
  pages?: number;
  /**
   * 总记录数
   * @type {number}
   * @memberof PageResultModel
   */
  total: number;
}
