import { ROUTER_PERMISSION_TYPE } from '@/router/router-const';

export const user = [
  {
    path: '/user/change-password',
    name: 'UserChangePassword',
    meta: {
      title: '修改密码',
      permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
    },
    component: () =>
      import('@/views/mine/change-password')
  }
];

