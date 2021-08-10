import { ROUTER_PERMISSION_TYPE } from '@/router/router-const';

export const loginRouter = [
  {
    path: '/login',
    name: 'Login',
    meta: {
      title: '登录',
      keepAlive: false,
      permissionType: ROUTER_PERMISSION_TYPE.NO_VALID.value
    },
    component: () => import('@/views/login/login.vue')
  }

];
