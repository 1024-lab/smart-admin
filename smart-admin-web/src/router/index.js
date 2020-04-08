import Vue from 'vue';
import Router from 'vue-router';
import { routers } from './routers';
import store from '@/store';
import ViewUI from 'view-design';
import cookie from '@/lib/cookie';
import { localRead } from '@/lib/local';
import { setTitle } from '@/lib/menu-func';
import config from '@/config';

const { homeName } = config;

Vue.use(Router); 
const router = new Router({
  // routes: routers,
  routes: buildRouters(routers)
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
  ViewUI.LoadingBar.start();
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
      // 跳转到home页
      name: homeName
    });
    setTitle(to, router.app);
    ViewUI.LoadingBar.finish();
    window.scrollTo(0, 0);
  } else {
    // 特殊页面直接放行
    if (to.meta.noValidatePrivilege) {
      next();
      return;
    }

    //如果是超管，直接放行
    if (store.state.user.userLoginInfo.isSuperMan) {
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
  ViewUI.LoadingBar.finish();
  window.scrollTo(0, 0);
});

function buildRouters (routerArray) {
  let lineRouters = [];
  for (let routerItem of routerArray) {
    //如果是顶层菜单
    if (routerItem.meta.topMenu) {
      // for (let children of routerItem.children) {
      let lineRouterArray = convertRouterTree2Line(routerItem.children);
      lineRouters.push(...lineRouterArray);
      // }
    } else {
      let lineRouterArray = convertRouterTree2Line([routerItem]);
      lineRouters.push(...lineRouterArray);
    }
  }
  return lineRouters;
}

function convertRouterTree2Line (routerArray) {
  //一级,比如 人员管理
  let topArray = [];
  for (let router1Item of routerArray) {
    let level2Array = [];
    //二级，比如员工管理
    if (router1Item.children) {
      for (let level2Item of router1Item.children) {

        let level2ItemCopy = {};
        for (let property in level2Item) {
          if ('children' !== property) {
            level2ItemCopy[property] = level2Item[property];
          }
        }

        //三级，
        if (level2Item.children) {
          level2Array.push(...level2Item.children)
        }

        level2ItemCopy.children = [];
        level2Array.push(level2Item);
      }
    }

    let newTopRouterItem = {};
    for (let property in router1Item) {
      if ('children' !== property) {
        newTopRouterItem[property] = router1Item[property];
      }
    }

    newTopRouterItem.children = level2Array;
    topArray.push(newTopRouterItem);
  }

  return topArray;
}

let tempCheckObj = {
  checkRouterNameMap: new Map(),
  checkRouterPathMap: new Map()
};

function recursionCheckRouter (routerArray) {
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
      recursionCheckRouter(routerItem.children);
    }
  }
}

//如果是开发环境，需要检测router的规范性
if (process.env.NODE_ENV === 'development') {
  recursionCheckRouter(routers);
  delete tempCheckObj.checkRouterNameMap;
  delete tempCheckObj.checkRouterPathMap;
}

const topMenuArray = routers.filter(e => e.meta.topMenu);
export { topMenuArray };

export default router;

