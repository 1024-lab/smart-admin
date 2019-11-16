import { postAxios, getAxios } from '@/lib/http';
export const emailApi = {
  // 新增邮件
  addEmail: (data) => {
    return postAxios('/email/add', data);
  },
  // 分页查询邮件
  getEmail: (data) => {
    return postAxios('/email/page/query', data);
  },
  // 删除邮件
  deleteEmail: (id) => {
    return getAxios('/email/delete/' + id);
  },
  // 查看邮件详情
  getEmailDetails: (id) => {
    return getAxios('/email/detail/' + id);
  },
  // 发送邮件
  sendEmail: (id) => {
    return getAxios('/email/send/' + id);
  },
  // 更新编辑邮件
  updateEmail: (data) => {
    return postAxios('/email/update', data);
  }
};
