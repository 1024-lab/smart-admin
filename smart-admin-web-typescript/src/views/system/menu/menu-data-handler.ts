/*
 * 此文件是处理 菜单数据的类，主要用于：
 * 1、菜单树形表格的构造
 * 2、菜单的前端过滤
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-06-15 16:47:20
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */

import _ from 'lodash';
/**
 * 过滤菜单
 * @param {*} menuList
 * @param {*} queryForm
 * @returns
 */
export const filterMenuByQueryForm = (menuList, queryForm) => {
  if (!menuList || menuList.length === 0) {
    return [];
  }

  let filterResult = [];
  for (const menu of menuList) {
    if (isMenuExistKeywords(menu, queryForm.keywords) && isMenuExistMenuType(menu, queryForm.menuType) && isMenuExistMenuFlag(menu, queryForm)) {
      filterResult.push(menu);
    }
  }
  return filterResult;
};

/**
 * 构建菜单表格树形数据
 */
export const buildMenuTableTree = (menuList) => {
  let topMenuList = [];
  const menuIdSet = new Set();
  for (const menu of menuList) {
    menuIdSet.add(menu.menuId);
  }

  for (const menu of menuList) {
    const parentId = menu.parentId;
    // 不存在父节点，则为顶级菜单
    if (!menuIdSet.has(parentId)) {
      topMenuList.push(menu);
    }
  }

  recursiveMenuTree(menuList, topMenuList);
  return topMenuList;
};

/**
 * 递归遍历菜单树形数据
 * @param {*} menuList
 * @param {*} parentArray
 */
function recursiveMenuTree(menuList, parentArray) {
  for (const parent of parentArray) {
    const children = menuList.filter((e) => e.parentId === parent.menuId);
    if (children.length > 0) {
      parent.children = children;
      recursiveMenuTree(menuList, parent.children);
    }
  }
}

/**
 * 过滤菜单状态
 * @param {*} menu
 * @param {*} queryForm
 * @returns
 */
function isMenuExistMenuFlag(menu, queryForm) {
  let frameFlagCondition = false;
  if (!_.isNil(queryForm.frameFlag)) {
    frameFlagCondition = !_.isNil(menu.frameFlag) && menu.frameFlag === (queryForm.frameFlag === 1);
  } else {
    frameFlagCondition = true;
  }

  let cacheFlagCondition = false;
  if (!_.isNil(queryForm.cacheFlag)) {
    cacheFlagCondition = !_.isNil(menu.cacheFlag) && menu.cacheFlag === (queryForm.cacheFlag === 1);
  } else {
    cacheFlagCondition = true;
  }

  let visibleFlagCondition = false;
  if (!_.isNil(queryForm.visibleFlag)) {
    visibleFlagCondition = !_.isNil(menu.visibleFlag) && menu.visibleFlag === (queryForm.visibleFlag === 1);
  } else {
    visibleFlagCondition = true;
  }

  let disabledFlagCondition = false;
  if (!_.isNil(queryForm.disabledFlag)) {
    disabledFlagCondition = !_.isNil(menu.disabledFlag) && menu.disabledFlag === (queryForm.disabledFlag === 1);
  } else {
    disabledFlagCondition = true;
  }

  return frameFlagCondition && cacheFlagCondition && visibleFlagCondition && disabledFlagCondition;
}

/**
 * 过滤菜单类型
 * @param {*} menu
 * @param {*} menuType
 * @returns
 */
function isMenuExistMenuType(menu, menuType) {
  if (!menuType) {
    return true;
  }

  if (menu.menuType && menu.menuType === menuType) {
    return true;
  }
  return false;
}

/**
 * 过滤关键字
 */
function isMenuExistKeywords(menu, keywords) {
  if (!keywords) {
    return true;
  }

  if (menu.component && menu.component.indexOf(keywords) > -1) {
    return true;
  }

  if (menu.menuName && menu.menuName.indexOf(keywords) > -1) {
    return true;
  }
  if (menu.path && menu.path.indexOf(keywords) > -1) {
    return true;
  }
  if (menu.apiPerms && menu.apiPerms.indexOf(keywords) > -1) {
    return true;
  }
  if (menu.webPerms && menu.webPerms.indexOf(keywords) > -1) {
    return true;
  }
  return false;
}
