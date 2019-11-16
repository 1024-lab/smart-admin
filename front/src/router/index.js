import Vue from 'vue';
import Router from 'vue-router';
import { routers } from './routers';
import store from '@/store';
import iView from 'iview';
import cookie from '@/lib/cookie';
import { localRead } from '@/lib/local';
import { setTitle } from '@/lib/menu-func';

Vue.use(Router);
const router = new Router({
  routes: routers
  // mode: 'history'
});
const LOGIN_PAGE_NAME = 'login';

// 防止用户刷新丢失登录信息
if (
  Object.keys(store.state.user.userLoginInfo).length === 0 &&
  localRead('userLoginInfo')
) {
  store.commit('setUserLoginInfo', JSON.parse(localRead('userLoginInfo')));
}
// 解决路由跳转相同的地址报错
const originalPush = Router.prototype.push;
Router.prototype.push = function (location) {
  try {
    return originalPush.call(this, location).catch(err => err);
  } catch (error) {
    // TODO zhuoda sentry
    console.error(error);
  }
};

// 关于当前页面
Router.prototype.closeCurrentPage = function () {
  let current = this.history.current;
  store.commit('closeTag', current);
  store.commit('deleteKeepAliveIncludes', current.name);
};

// 关闭当前页面然后跳转到指定页面
Router.prototype.closeCurrentPageAndPush = function (pushParam) {
  let current = this.history.current;
  store.commit('closeTagNotPushNextRoute', current);
  store.commit('deleteKeepAliveIncludes', current.name);
  this.push(pushParam);
};
let storeSelf = store;
router.beforeEach((to, from, next) => {
  console.log(to);
  iView.LoadingBar.start();
  const token = cookie.getToken();
  if (!token && to.name !== LOGIN_PAGE_NAME) {
    // 未登录且要跳转的页面不是登录页
    next({
      name: LOGIN_PAGE_NAME // 跳转到登录页
    });
  } else if (!token && to.name === LOGIN_PAGE_NAME) {
    // 未登陆且要跳转的页面是登录页
    next(); // 跳转
  } else if (token && to.name === LOGIN_PAGE_NAME) {
    // 已登录且要跳转的页面是登录页
    next({
      name: 'home' // 跳转到home页
    });
  } else {
    // 特殊页面直接放行
    if (to.meta.access) {
      next();
      return;
    }

    // 去掉/之后第一个字母
    let key = to.path.substr(1, 1);
    let pathArray = storeSelf.state.user.privilegeRouterPathMap.get(key);
    if (!(pathArray && pathArray.indexOf(to.path) >= 0)) {
      next({
        name: 'Error401'
      });
    } else {
      next();
    }
  }
});

router.afterEach(to => {
  setTitle(to, router.app);
  iView.LoadingBar.finish();
  window.scrollTo(0, 0);
});

let tempCheckObj = {
  checkRouterNameMap: new Map(),
  checkRouterPathMap: new Map()
};

function recursionRouter (routerArray) {
  for (let routerItem of routerArray) {
    if (!routerItem.name) {
      console.error('没有配置router name', routerItem);
    } else {
      let existNameRouter = tempCheckObj.checkRouterNameMap.get(
        routerItem.name
      );
      if (typeof existNameRouter !== 'undefined') {
        console.error('存在相同的router name', routerItem, existNameRouter);
      } else {
        tempCheckObj.checkRouterNameMap.set(routerItem.name, routerItem);
      }
    }

    if (!routerItem.path) {
      console.error('没有配置router path', routerItem);
    } else {
      // path必须以 / 开头
      if (routerItem.path !== '*' && routerItem.path.indexOf('/') !== 0) {
        console.error('path 没有以/开头 ', routerItem);
      }

      let existPathRouter = tempCheckObj.checkRouterPathMap.get(
        routerItem.path
      );
      if (typeof existPathRouter !== 'undefined') {
        console.error('存在相同的router path', routerItem, existPathRouter);
      } else {
        tempCheckObj.checkRouterPathMap.set(routerItem.path, routerItem);
      }
    }

    if (routerItem.children) {
      recursionRouter(routerItem.children);
    }
  }
}

recursionRouter(routers);

delete tempCheckObj.checkRouterNameMap;
delete tempCheckObj.checkRouterPathMap;

export default router;
