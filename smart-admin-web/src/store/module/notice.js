import { noticeApi } from '@/api/notice';
export default {
  state: {
    noticeList: [],
    noticeNumber: 0
  },
  mutations: {
    updateNotice(state, data) {
      state.noticeList = [...state.noticeList, ...data];
    },
    updateNoticeNum(state, data) {
      state.noticeNumber = data;
    },
    restNotice(state) {
      state.noticeList = [];
      state.noticeNumber = 0;
    }
  }
};
