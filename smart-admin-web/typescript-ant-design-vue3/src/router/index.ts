/*
 * 路由
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:52:04
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */

import nProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { nextTick } from 'vue';
import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';
import { routerArray } from './routers';
import { PAGE_PATH_404, PAGE_PATH_LOGIN } from '/@/constants/common-const';
import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
import SmartLayout from '/@/layout/smart-layout.vue';
import { useUserStore } from '/@/store/modules/system/user';
import { clearAllCoolies, getTokenFromCookie } from '/@/utils/cookie-util';
import { localClear } from '/@/utils/local-util';

export const router = createRouter({
  history: createWebHashHistory(),
  routes: routerArray,
  strict: true,
  scrollBehavior: () => ({ left: 0, top: 0 }),
});

// ----------------------- 路由加载前 -----------------------
router.beforeEach(async (to, from, next) => {
  // 进度条开启
  nProgress.start();

  // 公共页面，任何时候都可以跳转
  if (to.path === PAGE_PATH_404 || to.path === PAGE_PATH_LOGIN) {
    next();
    return;
  }

  // 验证登录
  const token = getTokenFromCookie();
  if (!token) {
    clearAllCoolies();
    localClear();
    next({ path: PAGE_PATH_LOGIN });
    return;
  }

  // 首页（ 需要登录 ，但不需要验证权限）
  if (to.path == HOME_PAGE_NAME) {
    next();
    return;
  }

  // 是否刷新缓存
  // 当前路由是否在tag中 存在tag中且没有传递keepAlive则刷新缓存
  let findTag = (useUserStore().tagNav || []).find((e) => e.menuName == to.name);
  let reloadKeepAlive = findTag && !to.params.keepAlive;

  // 设置tagNav
  useUserStore().setTagNav(to, from);
  // 设置keepAlive 或 删除KeepAlive
  if (to.meta.keepAlive) {
    if (reloadKeepAlive) {
      useUserStore().deleteKeepAliveIncludes(to.name?.toString());
    }
    nextTick(() => {
      useUserStore().pushKeepAliveIncludes(to.name?.toString());
    });
  }
  next();
});

// ----------------------- 路由加载后 -----------------------
router.afterEach(() => {
  nProgress.done();
});

// ----------------------- 构建router对象 -----------------------
const routerMap = new Map();
export function buildRoutes(menuRouterList) {
  let menuList = menuRouterList ? menuRouterList : useUserStore().getMenuRouterList || [];
  /**
   * 1、构建整个路由信息
   * 2、添加到路由里
   */
  const resList = [];
  // 获取所有vue组件引用地址 用于构建路由
  const modules = import.meta.glob('../views/**/**.vue');
  // 获取所有vue组件 用于注入name属性 name属性用于keep-alive
  // const modulesEager = import.meta.globEager('../views/**/**.vue');

  //1、构建整个路由信息
  for (const e of menuList) {
    if (!e.menuId) {
      continue;
    }
    if (!e.path) {
      continue;
    }
    if (e.deletedFlag && e.deletedFlag === 1) {
      continue;
    }
    let menuIdStr = e.menuId.toString();
    let route = {
      path: e.path.startsWith('/') ? e.path : `/${e.path}`,
      // 使用menuId作为name唯一标识
      name: menuIdStr,
      meta: {
        // 菜单展示
        title: e.menuName,
        // 菜单图标展示
        icon: e.icon,
        // 是否在菜单隐藏
        hideInMenu: !e.visibleFlag,
        // 页面是否keep-alive缓存
        keepAlive: e.cacheFlag,
        // 是否为外链
        frameFlag: e.frameFlag,
        // 外链地址
        frameUrl: e.frameUrl,
      },
    };

    if (e.frameFlag) {
      route.component = () => import('../components/framework/iframe/route-default-component.vue');
      resList.push(route);
      continue;
    } else {
      let componentPath = e.component && e.component.startsWith('/') ? e.component : '/' + e.component;
      let relativePath = `../views${componentPath}`;
      // eslint-disable-next-line no-prototype-builtins
      route.component = modules[relativePath];
    }
    // 注释无用逻辑
    // if (modules.hasOwnProperty(relativePath)) {
    //   route.component = modules[relativePath];
    //   // 组件注入name
    //   let eager = modulesEager[relativePath];
    //   if (eager) {
    //     eager.default.name = menuIdStr;
    //   }
    // }
    resList.push(route);
    routerMap.set(e.menuId.toString(), route);
  }

  //2、添加到路由里
  router.addRoute({
    path: '/',
    meta: {},
    component: SmartLayout,
    children: resList,
  });
}
