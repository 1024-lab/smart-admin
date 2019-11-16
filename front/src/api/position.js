import { postAxios, getAxios } from '@/lib/http';
export const positionApi = {
  // 分页查询所有岗位
  getPositionListPage: data => {
    return postAxios('/position/getListPage', data);
  },
  // 更新岗位
  updatePosition: data => {
    return postAxios('/position/update', data);
  },
  // 添加岗位
  addPosition: data => {
    return postAxios('/position/add', data);
  },
  // 根据ID删除岗位
  deletePosition: id => {
    return getAxios('/position/remove/' + id);
  }
};
