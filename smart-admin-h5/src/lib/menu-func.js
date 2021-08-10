import { forEach, hasOneOf, objEqual } from '@/lib/util';
import config from '@/config';
import { localRead, localSave } from '@/lib/local';
const { title, useI18n } = config;
export const hasChild = item => {
  return item.children && item.children.length !== 0;
};

/**
 * 通过权限过滤菜单
 * @param {Object} map 权限对象
 * @param {Array} menuList 菜单列表
 * @returns {Array}
 */
export const getShowMenu = (map = {}, menuList, access = false) => {
  // 判断是否为超级管理员
  if (access) {
    return menuList;
  }
  // 返回的菜单列表
  let result = [];
  for (let menuItem of menuList) {
    let routerObj = JSON.parse(JSON.stringify(menuItem));
    if (
      map.hasOwnProperty(menuItem.name) &&
      (menuItem.name !== 'home' && menuItem.name !== '_home')
    ) {
      // 判断该菜单权限下是否为数组，若为数组，则为功能点权限否则为子菜单
      if (getType(map[routerObj.name]) === 'array') {
        let funcPrivilege = localRead('funcPrivilegeInfo')
          ? JSON.parse(localRead('funcPrivilegeInfo'))
          : {};
        localSave(
          'funcPrivilegeInfo',
          JSON.stringify({
            ...funcPrivilege,
            [routerObj.name]: map[routerObj.name]
          })
        );
      } else if (
        getType(map[routerObj.name]) !== 'array' &&
        !routerObj.children
      ) {
        // 判断是否为二级菜单，若是则需要多枚举一层赋值
        let funcPrivilege = localRead('funcPrivilegeInfo')
          ? JSON.parse(localRead('funcPrivilegeInfo'))
          : {};
        localSave(
          'funcPrivilegeInfo',
          JSON.stringify({
            ...funcPrivilege,
            [routerObj.name]: map[routerObj.name][routerObj.name]
          })
        );
      } else if (
        getType(map[routerObj.name]) !== 'array' &&
        routerObj.children
      ) {
        // 循环子菜单权限
        routerObj.children = getShowMenu(
          map[routerObj.name],
          routerObj.children
        );
      }
      result.push(routerObj);
    }
  }
  return result;
};
// 获取数据类型
export const getType = obj => {
  return {}.toString
    .call(obj)
    .match(/\s([a-zA-Z]+)/)[1]
    .toLowerCase();
};

/**
 * @description 本地存储和获取标签导航列表
 */
export const setTagNavListInLocalStorage = list => {
  localStorage.tagNaveList = JSON.stringify(list);
};
/**
 * @returns {Array} 其中的每个元素只包含路由原信息中的name, path, meta三项
 */
export const getTagNavListFromLocalStorage = () => {
  const list = localStorage.tagNaveList;
  return list ? JSON.parse(list) : [];
};
export const getBreadCrumbList = (route, homeRoute) => {
  let homeItem = {
    ...homeRoute,
    icon: homeRoute.meta.icon
  };
  let routeMatched = route.matched;
  if (routeMatched.some(item => item.name === homeRoute.name)) {
    return [homeItem];
  }
  let res = routeMatched
    .filter(item => {
      return item.meta === undefined || !item.meta.hideInBread;
    })
    .map(item => {
      let meta = {
        ...item.meta
      };
      if (meta.title && typeof meta.title === 'function') {
        meta.__titleIsFunction__ = true;
        meta.title = meta.title(route);
      }
      let obj = {
        icon: (item.meta && item.meta.icon) || '',
        name: item.name,
        meta: meta
      };
      return obj;
    });
  res = res.filter(item => {
    return !item.meta.hideInMenu;
  });
  return [...res];
};
/**
 * @param {Array} routers 路由列表数组
 * @description 用于找到路由列表中name为home的对象
 */
export const getHomeRoute = (routers, homeName = 'Home') => {
  let i = -1;
  let len = routers.length;
  let homeRoute = {};
  while (++i < len) {
    let item = routers[i];
    if (item.children && item.children.length) {
      let res = getHomeRoute(item.children, homeName);
      if (res.name) return res;
    } else {
      if (item.name === homeName) homeRoute = item;
    }
  }
  return homeRoute;
};
/**
 * @param {Array} list 标签列表
 * @param {String} name 当前关闭的标签的name
 */
export const getNextRoute = (list, route) => {
  let res = {};
  if (list.length === 2) {
    res = getHomeRoute(list);
  } else {
    const index = list.findIndex(item => routeEqual(item, route));
    if (index === list.length - 1) res = list[list.length - 2];
    else res = list[index + 1];
  }
  return res;
};

/**
 * 判断打开的标签列表里是否已存在这个新添加的路由对象
 */
export const routeHasExist = (tagNavList, routeItem) => {
  let len = tagNavList.length;
  let res = false;
  doCustomTimes(len, index => {
    if (routeEqual(tagNavList[index], routeItem)) res = true;
  });
  return res;
};
/**
 * @param {*} list 现有标签导航列表
 * @param {*} newRoute 新添加的路由原信息对象
 * @description 如果该newRoute已经存在则不再添加
 */
export const getNewTagList = (list, newRoute) => {
  const { name, path, meta, query } = newRoute;
  let newList = [...list];
  let index = newList.findIndex(item => item.name === name);
  if (index >= 0) {
    newList[index] = { name, path, meta, query };
  } else newList.push({ name, path, meta, query });
  return newList;
};
export const routeEqual = (route1, route2) => {
  return route1.name === route2.name;
};
export const getRouteTitleHandled = route => {
  let router = {
    ...route
  };
  let meta = {
    ...route.meta
  };
  let title = '';
  if (meta.title) {
    if (typeof meta.title === 'function') {
      meta.__titleIsFunction__ = true;
      title = meta.title(router);
    } else title = meta.title;
  }
  meta.title = title;
  router.meta = meta;
  return router;
};
/**
 * @param {Number} times 回调函数需要执行的次数
 * @param {Function} callback 回调函数
 */
export const doCustomTimes = (times, callback) => {
  let i = -1;
  while (++i < times) {
    callback(i);
  }
};
export const showTitle = (item, vm) => {
  let { title, __titleIsFunction__ } = item.meta;
  if (!title) return;
  if (useI18n) {
    if (title.includes('{{') && title.includes('}}') && useI18n) {
      title = title.replace(/({{[\s\S]+?}})/, (m, str) =>
        str.replace(/{{([\s\S]*)}}/, (m, _) => vm.$t(_.trim()))
      );
    } else if (__titleIsFunction__) title = item.meta.title;
    else title = vm.$t(item.name);
  } else title = (item.meta && item.meta.title) || item.name;
  return title;
};
/**
 * @description 根据当前跳转的路由设置显示在浏览器标签的title
 * @param {Object} routeItem 路由对象
 * @param {Object} vm Vue实例
 */
export const setTitle = (routeItem, vm) => {
  const handledRoute = getRouteTitleHandled(routeItem);
  const pageTitle = showTitle(handledRoute, vm);
  const resTitle = pageTitle ? `${pageTitle} - ${title}` : title;
  window.document.title = resTitle;
};

export const findNodeUpper = (ele, tag) => {
  if (ele.parentNode) {
    if (ele.parentNode.tagName === tag.toUpperCase()) {
      return ele.parentNode;
    } else {
      return findNodeUpper(ele.parentNode, tag);
    }
  }
};

export const findNodeUpperByClasses = (ele, classes) => {
  let parentNode = ele.parentNode;
  if (parentNode) {
    let classList = parentNode.classList;
    if (
      classList &&
      classes.every(className => classList.contains(className))
    ) {
      return parentNode;
    } else {
      return findNodeUpperByClasses(parentNode, classes);
    }
  }
};

export const findNodeDownward = (ele, tag) => {
  const tagName = tag.toUpperCase();
  if (ele.childNodes.length) {
    let i = -1;
    let len = ele.childNodes.length;
    while (++i < len) {
      let child = ele.childNodes[i];
      if (child.tagName === tagName) return child;
      else return findNodeDownward(child, tag);
    }
  }
};
