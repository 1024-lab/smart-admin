// 页面内按钮过滤
import store from '@/store/index';
export default {
  inserted: function (el, binding, vnode) {
    // 获取当前路由name
    // 如果页面为同一模块下的子页面则取最上级权限
    let routeName = vnode.context.$route.meta.group
      ? vnode.context.$route.meta.group
      : vnode.context.$route.name;
    // 超级管理员
    if (store.state.user.userLoginInfo.isSuperMan) {
      return true;
    }
    // 获取功能点权限
    let functionList = store.state.user.privilegeFunctionPointsMap.get(routeName);
    // 有权限
    if (functionList && functionList.includes(binding.value)) {

    } else {
      el.parentNode.removeChild(el);
    }
  }
};
