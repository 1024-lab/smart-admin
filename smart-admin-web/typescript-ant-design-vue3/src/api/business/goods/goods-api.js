/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const goodsApi = {
  // 添加商品 @author zhuoda
  addGoods: (param) => {
    return postRequest('/goods/add', param);
  },
  // 删除 @author zhuoda
  deleteGoods: (goodsId) => {
    return getRequest(`/goods/delete/${goodsId}`);
  },
  // 批量 @author zhuoda
  batchDelete: (goodsIdList) => {
    return postRequest('/goods/batchDelete', goodsIdList);
  },
  // 分页查询 @author zhuoda
  queryGoodsList: (param) => {
    return postRequest('/goods/query', param);
  },
  // 更新商品 @author zhuoda
  updateGoods: (param) => {
    return postRequest('/goods/update', param);
  },
};
