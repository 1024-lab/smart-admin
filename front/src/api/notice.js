// 任务调度API
import {
  postAxios,
  getAxios
} from '@/lib/http';
export const noticeApi = {
  // 查询消息列表
  getNoticeList: (data) => {
    return postAxios('/notice/page/query', data);
  },
  // 未读消息列表
  getNoticeUnreadList: (data) => {
    return postAxios('/notice/unread/page/query', data);
  },
  // 查询个人消息列表
  getPersonNoticeList: (data) => {
    return postAxios('/notice/receive/page/query', data);
  },
  // 添加消息
  addNotice: (data) => {
    return postAxios('/notice/add', data);
  },
  // 标记已读
  addNoticeRecord: (id) => {
    return getAxios(`/notice/read/${id}`);
  },
  // 修改消息
  updateNotice: (data) => {
    return postAxios('/notice/update', data);
  },
  // 删除消息
  deleteNotice: (id) => {
    return getAxios(`/notice/delete/${id}`);
  },
  // 获取通知详情
  getNoticeDetail: (id) => {
    return getAxios(`/notice/detail/${id}`);
  },
  // 发送消息
  sendNotice: (id) => {
    return getAxios(`/notice/send/${id}`);
  }
};
