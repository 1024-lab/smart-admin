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
import { createRouter, createWebHashHistory } from 'vue-router';
import { routerArray } from './routers';
import { PAGE_PATH_404, PAGE_PATH_LOGIN } from '/@/constants/common-const';
import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
import SmartLayout from '/@/layout/smart-layout.vue';
import { useUserStore } from '/@/store/modules/system/user';
import { clearAllCoolies, getTokenFromCookie } from '/@/utils/cookie-util';
import { localClear } from '/@/utils/local-util';
import lodash from 'lodash';

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

  // 下载路由对应的 页面组件，并修改组件的Name，如果修改过，则不需要修改
  let toRouterInfo = routerMap.get(to.name);
  if (toRouterInfo && lodash.isFunction(toRouterInfo.component) && toRouterInfo.meta.renameComponentFlag === false) {
    // 因为组件component 为 lazy load是个方法，所以可以直接执行 component()方法
    toRouterInfo.component().then((val) => {
      // 修改组件的name
      val.default.name = to.meta.componentName;
      // 记录已经修改过 组件的name
      toRouterInfo.meta.renameComponentFlag = true;
    });
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
      useUserStore().deleteKeepAliveIncludes(to.meta.componentName);
    }
    nextTick(() => {
      useUserStore().pushKeepAliveIncludes(to.meta.componentName);
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
  const routerList = [];
  // 获取所有vue组件引用地址 用于构建路由
  const modules = import.meta.glob('../views/**/**.vue');
  // 获取所有vue组件 用于注入name属性 name属性用于keep-alive

  //1、构建整个路由信息
  for (const e of menuList) {
    if (!e.menuId) {
      continue;
    }
    if (!e.path) {
      continue;
    }
    if (e.deletedFlag && e.deletedFlag === true) {
      continue;
    }
    let componentName = e.menuId.toString();
    if (e.component) {
      let lastIndex = e.component.lastIndexOf('/');
      let fileName = lodash.camelCase(e.component.substring(lastIndex + 1, e.component.length));
      componentName = lodash.camelCase(fileName) + componentName;
      componentName = lodash.upperFirst(componentName);
    }

    let route = {
      path: e.path.startsWith('/') ? e.path : `/${e.path}`,
      // 使用【组件文件名+menuId】作为name唯一标识
      name: e.menuId.toString(),
      meta: {
        // 数据库菜单(页面)id
        id: e.menuId.toString(),
        // 组件名称
        componentName: componentName,
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
        // 是否 rename了组件的名字
        renameComponentFlag: false,
      },
    };

    if (e.frameFlag) {
      route.component = () => import('../components/framework/iframe/iframe-index.vue');
      
    } else {
      let componentPath = e.component && e.component.startsWith('/') ? e.component : '/' + e.component;
      let relativePath = `../views${componentPath}`;
      // // eslint-disable-next-line no-prototype-builtins
      route.component = modules[relativePath];
    }
    routerList.push(route);
    routerMap.set(e.menuId.toString(), route);
  }

  //2、添加到路由里
  router.addRoute({
    path: '/',
    meta: {},
    component: SmartLayout,
    children: routerList,
  });
}
