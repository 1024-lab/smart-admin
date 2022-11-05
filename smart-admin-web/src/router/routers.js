import { home } from './module/home';
import { error } from './module/error';
import { business } from './module/business';
import { support } from './module/support';
import { system } from './module/system';

/**
 *
 * meta除了原生参数外可配置的参数:
 * meta: {
 *  title: { String|Number|Function }
 *         显示在侧边栏、面包屑和标签栏的文字
 *         使用'{{ 多语言字段 }}'形式结合多语言使用，例子看多语言的路由配置;
 *         可以传入一个回调函数，参数是当前路由对象，例子看动态路由和带参路由
 *  hideInBread: (false) 设为true后此级路由将不会出现在面包屑中，示例看QQ群路由配置
 *  hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项,
 *  privilegeExtend:{String} 同一功能模块下子页面的功能点权限继承菜单模块创建的路由权限  by lihaifan&lipeng
 *  noKeepAlive: (false) 设为true后页面在切换标签后不会缓存，如果需要缓存，无需设置这个字段，而且需要设置页面组件name属性和路由配置的name一致
 *  noValidatePrivilege: (true) 表示此路由不需要验证权限
 *  topMenu:(true)，表示为顶级菜单
 * }
 */
// 登录模块
export const login = {
  path: '/login',
  name: 'login',
  meta: {
    hideInMenu: true,
    title: 'Login - 登录',
    noValidatePrivilege: true
  },
  component: () => import('@/views/login/login.vue')
};

// 全部路由
export const routers = [
  login,
  ...home,
  ...error,
  ...business, 
  ...system,
  ...support
];
