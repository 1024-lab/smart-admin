import {
  getBreadCrumbList,
  setTagNavListInLocalStorage,
  getMenuByRouter,
  getTagNavListFromLocalStorage,
  getHomeRoute,
  getNextRoute,
  routeHasExist,
  routeEqual,
  getRouteTitleHandled
} from '@/lib/menu-func';
import { localSave, localRead } from '@/lib/local';
import router from '@/router';
import { routers } from '@/router/routers';
import config from '@/config';
const { homeName } = config;
// 关闭页面+tag
const closePage = (state, route) => {
  const nextRoute = getNextRoute(state.tagNavList, route);
  state.tagNavList = state.tagNavList.filter(item => {
    return !routeEqual(item, route);
  });
  router.push(nextRoute);
};

export default {
  state: {
    // 缓存路由
    keepAliveIncludes: [],
    // 面包屑列表
    breadCrumbList: [],
    // tag列表
    tagNavList: [],
    // 首页路由
    homeRoute: {},
    // 本地缓存
    local: localRead('local'),
    // 错误列表
    errorList: [],
    hasReadErrorPage: false
  },
  getters: {

    errorCount: state => state.errorList.length
  },
  mutations: {
    // 加入缓存
    pushKeepAliveIncludes (state, val) {
      if (state.keepAliveIncludes.length < 30) {
        let number = state.keepAliveIncludes.findIndex(e => e === val);
        if (number === -1) {
          state.keepAliveIncludes.push(val);
        }
      }
    },
    // 删除缓存
    deleteKeepAliveIncludes (state, val) {
      let number = state.keepAliveIncludes.findIndex(e => e === val);
      if (number !== -1) {
        state.keepAliveIncludes.splice(number, 1);
      }
    },
    // 清空缓存
    clearKeepAliveIncludes (state, val) {
      state.keepAliveIncludes = [val];
    },
    // 关闭其他
    deleteOtherKeepAliveIncludes (state, val) {
      state.keepAliveIncludes.forEach((item, index, arr) => {
        if (item !== config.homeName && item !== val) {
          arr.splice(index, 1);
        }
      });
    },
    // 设置其是否出现在面包屑中
    setBreadCrumb (state, route) {
      state.breadCrumbList = getBreadCrumbList(route, state.homeRoute);
    },
    // 初始化首页使用
    setHomeRoute (state, routes) {
      state.homeRoute = getHomeRoute(routes, homeName);
    },
    // 设置tag列表
    setTagNavList (state, list) {
      let tagList = [];
      if (list) {
        tagList = [...list];
      } else tagList = getTagNavListFromLocalStorage() || [];
      if (tagList[0] && tagList[0].name !== homeName) tagList.shift();
      let homeTagIndex = tagList.findIndex(item => item.name === homeName);
      if (homeTagIndex > 0) {
        let homeTag = tagList.splice(homeTagIndex, 1)[0];
        tagList.unshift(homeTag);
      }
      state.tagNavList = tagList;
      setTagNavListInLocalStorage([...tagList]);
    },
    // 关闭tag
    closeTag (state, route) {
      let tag = state.tagNavList.filter(item => routeEqual(item, route));
      route = tag[0] ? tag[0] : null;
      if (!route) return;
      closePage(state, route);
    },
    // 关闭当前tag，且不进行跳转
    closeTagNotPushNextRoute (state, route) {
      state.tagNavList = state.tagNavList.filter(item => {
        return !routeEqual(item, route);
      });
    },
    // 新增tag
    addTag (state, { route, type = 'unshift' }) {
      let router = getRouteTitleHandled(route);
      if (!routeHasExist(state.tagNavList, router)) {
        if (type === 'push') state.tagNavList.push(router);
        else {
          if (router.name === homeName) state.tagNavList.unshift(router);
          else state.tagNavList.splice(1, 0, router);
        }
        setTagNavListInLocalStorage([...state.tagNavList]);
      }
    },
    // 保存本地信息
    setLocal (state, lang) {
      localSave('local', lang);
      state.local = lang;
    }
  }
};
