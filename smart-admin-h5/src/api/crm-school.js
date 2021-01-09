import { getAxios, postAxios } from '@/lib/http';

export const crmSchoolApi = {

  // 分校列表 - 分校 by yandanyang
  querySchoolList: (data) => {
    return postAxios('/admin/crm/school/list', data);
  },
  // 分校跟进详列表 - 分校 by yandanyang
  querySchoolTrackList: data => {
    return postAxios('/admin/crm/school/track/list', data);
  },

  // 分校跟进 - 分校 by yandanyang
  addSchoolTrack: data => {
    return postAxios('/admin/crm/school/track/add', data);
  },
  // 分校跟进 - 分校 by yandanyang
  getSchoolTrackDetail: schoolTrackId => {
    return getAxios(`/admin/crm/school/track/detail/${schoolTrackId}`);
  }
};
