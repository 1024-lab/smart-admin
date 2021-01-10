import Vue from 'vue';
import Router from 'vue-router';
import { routers } from './routers';
import cookie from '@/lib/cookie';
import { ROUTER_PERMISSION_TYPE } from './router-const';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

const projectConfig = require('@/config/index.js');

Vue.use(Router);

/**
 * 导入所有的router
 * @type {VueRouter}
 */
const router = new Router({
  routes: routers
});

// 解决路由跳转相同的地址报错
const originalPush = Router.prototype.push;
Router.prototype.push = function(location) {
  try {
    return originalPush.call(this, location).catch(err => err);
  } catch (error) {
    // TODO zhuoda sentry
    console.error(error);
  }
};

const LOGIN_PAGE_NAME = 'Login';

// --------------------- router 守卫 begin ---------------------

router.beforeEach((to, from, next) => {
  // 加载进度条
  NProgress.start();

  // 权限
  const permissionType = to.meta.permissionType;
  // 不需要验证，直接放行
  if (permissionType === ROUTER_PERMISSION_TYPE.NO_VALID.value) {
    next();
    return;
  }

  const token = cookie.getToken();
  // 需要登录
  if (permissionType === ROUTER_PERMISSION_TYPE.NEED_LOGIN.value) {
    if (token) {
      next();
    } else {
      next({
        name: LOGIN_PAGE_NAME
      });
    }
    return;
  }

  // 需要登录，且验证权限
  if (permissionType === ROUTER_PERMISSION_TYPE.VALIDATE_PERMISSION.value) {
    if (!token) {
      // TODO 验证权限
      next({
        name: LOGIN_PAGE_NAME
      });
      return;
    }
  }

  next({
    name: 'Error404'
  });
});

router.afterEach(to => {
  NProgress.done();
  window.scrollTo(0, 0);
  if (to.meta.title) {
    console.log(to.meta);
    document.title = to.meta.title + ' ' + projectConfig.title;
  }
});

// --------------------- router 守卫 end ---------------------

/**
 * router 检测
 *
 * 如果存在相同的 path 或者 name 是一件非常恐怖的事情，所以在develop环境将所有router进行一次遍历<br>
 * 检测内容如下：<br>
 * 1、相同的router name
 * 2、相同的router name
 * 3、path没有以 / 开头
 *
 */

const tempCheckObj = {
  checkRouterNameMap: new Map(),
  checkRouterPathMap: new Map()
};

function recursionCheckRouter(routerArray) {
  for (const routerItem of routerArray) {
    if (!routerItem.name) {
      console.error('没有配置router name', routerItem);
    } else {
      const existNameRouter = tempCheckObj.checkRouterNameMap.get(
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

      const existPathRouter = tempCheckObj.checkRouterPathMap.get(
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

// 如果是开发环境，需要检测router的规范性
if (process.env.NODE_ENV === 'development') {
  recursionCheckRouter(routers);
  delete tempCheckObj.checkRouterNameMap;
  delete tempCheckObj.checkRouterPathMap;
}

export default router;
