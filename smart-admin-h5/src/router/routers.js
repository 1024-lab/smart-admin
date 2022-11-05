// 全局错误页面
import { errorRouter } from './error/error';
// 登录注册模块
import { loginRouter } from './login/login';
// tabbar dashboard 框架页面
import { dashboardRouter } from './dashboard';
// 用户相关
import { userRouter } from './user/user';
// 开发相关
import { developRouter } from './develop/develop';

/**
 * router meta 说明： <br>
 *
 * title: 为页面的title，会显示到浏览器的title上
 * permissionType: 具体使用 router-const.js中的  ROUTER_PERMISSION_TYPE 常量;情况有： 1)不验证 2)校验登录 3)登录后校验权限
 * keepAlive: true or false ; 是否进行页面keepalive, 如果想删除keepalive，可以使用vuex中的app module里有mutation
 * showTabbar: true or false ; 是否展示 tabbar, 如果是true， 则会展示tabbar
 *
 */
export const routers = [
  // 登录、注册
  ...loginRouter,
  // 404、500、403等
  ...errorRouter,
  // tab bar 页面
  ...dashboardRouter,
  // 用户
  ...userRouter,
  // 开发相关
  ...developRouter
];
