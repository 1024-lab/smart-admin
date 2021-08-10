import cookie from '@/lib/cookie.js';

export default {
  namespaced: true,
  state: {
    token: cookie.getToken(),
    // session 信息
    sessionInfo: {},
    // 是否获取了session
    isHaveGotSessionInfo: false,
    // 权限集合
    privilegeKeySet: new Set()

  },
  mutations: {
    clearSession() {
      state.token = null;
      state.sessionInfo = null;
      state.privilegeKeySet = new Set();
    },
    updateSession(state, userLoginInfo) {
      state.isHaveGotSessionInfo = true;
      state.sessionInfo = userLoginInfo;
      if (userLoginInfo.privilegeList) {
        state.privilegeKeySet = new Set(userLoginInfo.privilegeList.map(e => e.key));
      }
    }
  },
  getters: {
    // 用户菜单权限
    privilegeKeySet: state => state.privilegeKeySet,
    isSuperMan: state => state.sessionInfo.isSuperMan,
    actualName: state => state.sessionInfo.actualName,
    loginUserId: state => state.sessionInfo.id
  },
  actions: {
    // 登录
    handleLogin({ commit }, params) {
    }
  }
};
