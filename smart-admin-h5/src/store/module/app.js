/**
 * 整个应用相关的状态信息
 *
 * 比如： keepalive等
 */
export default {
  namespaced: true,
  state: {
    // 缓存路由
    keepAliveIncludes: []
  },

  mutations: {
    // 加入keep-alive缓存
    pushKeepAliveIncludes(state, val) {
      if (state.keepAliveIncludes.length < 30) {
        const number = state.keepAliveIncludes.findIndex(e => e === val);
        if (number === -1) {
          state.keepAliveIncludes.push(val);
        }
      }
    },
    // 删除缓存
    deleteKeepAliveIncludes(state, val) {
      const number = state.keepAliveIncludes.findIndex(e => e === val);
      if (number !== -1) {
        state.keepAliveIncludes.splice(number, 1);
      }
    }
  }

};
