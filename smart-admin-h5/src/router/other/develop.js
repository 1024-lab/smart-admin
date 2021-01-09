import { ROUTER_PERMISSION_TYPE } from '@/router/router-const';

export const develop = [
  {
    path: '/develop/config',
    name: 'DevelopConfig',
    meta: {
      title: '开发专用配置',
      permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
    },
    component: () =>
      import('@/views/develop/config')
  }
];

