import { tabbarRouter } from './tabbar';
import { otherRouter } from '@/router/other';
import { error } from './other/error';
import { ROUTER_PERMISSION_TYPE } from '@/router/router-const';

// 登录模块
export const login = {
  path: '/login',
  name: 'Login',
  meta: {
    title: '登录',
    keepAlive: false,
    permissionType: ROUTER_PERMISSION_TYPE.NO_VALID.value
  },
  component: () => import('@/views/login/login.vue')
};

// 全部路由
export const routers = [
  login,
  ...tabbarRouter,
  ...otherRouter,
  ...error
];
