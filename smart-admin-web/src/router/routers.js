import { home } from './module/home';
import { employee } from './module/employee';
import { systemSetting } from './module/system-setting';
import { notice } from './module/notice';
import { emailSetting } from './module/email';
import { monitor } from './module/monitor';
import { userLog } from './module/user-log';
import { error } from './module/error';
import { task } from './module/task';
import { reload } from './module/reload';
import { apiDoc } from './module/api-doc';
import { threeRouter } from './module/three-router';
import { keepAlive } from './module/keep-alive';
import { heartBeat } from './module/heart-beat';
import { file } from './module/file';

/**
 *
 * iview-admin中meta除了原生参数外可配置的参数:
 * meta: {
 *  title: { String|Number|Function }
 *         显示在侧边栏、面包屑和标签栏的文字
 *         使用'{{ 多语言字段 }}'形式结合多语言使用，例子看多语言的路由配置;
 *         可以传入一个回调函数，参数是当前路由对象，例子看动态路由和带参路由
 *  hideInBread: (false) 设为true后此级路由将不会出现在面包屑中，示例看QQ群路由配置
 *  hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项,
 *  group:{String} 同一功能模块下子页面的功能点权限继承菜单模块创建的路由权限  by lihaifan&lipeng
 *  noKeepAlive: (false) 设为true后页面在切换标签后不会缓存，如果需要缓存，无需设置这个字段，而且需要设置页面组件name属性和路由配置的name一致
 *  access: (null) 可访问该页面的权限数组，当前路由设置的权限会影响子路由
 * }
 */
// 登录模块
export const login = {
  path: '/login',
  name: 'login',
  meta: {
    hideInMenu: true,
    title: 'Login - 登录'
  },
  component: () => import('@/views/login/login.vue')
};

// 全部路由
export const routers = [
  login,
  ...home,
  ...employee,
  ...systemSetting,
  ...notice,
  ...emailSetting,
  ...userLog,
  ...monitor,
  ...error,
  ...task,
  ...reload,
  ...apiDoc,
  ...threeRouter,
  ...keepAlive,
  ...heartBeat,
  ...file
];
