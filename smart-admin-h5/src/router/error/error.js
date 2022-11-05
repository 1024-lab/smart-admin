// 错误页
import { ROUTER_PERMISSION_TYPE } from '@/router/router-const';

export const errorRouter = [
  {
    path: '/404',
    name: 'Error404',
    meta: {
      hideInMenu: true,
      access: true,
      permissionType: ROUTER_PERMISSION_TYPE.NO_VALID.value
    },
    component: () => import('views/error/404.vue')
  },
  {
    path: '/500',
    name: 'Error500',
    meta: {
      hideInMenu: true,
      access: true,
      noValidatePrivilege: true,
      permissionType: ROUTER_PERMISSION_TYPE.NO_VALID.value
    },
    component: () => import('views/error/404.vue')
  }
];
