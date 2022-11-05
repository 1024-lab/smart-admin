import { ROUTER_PERMISSION_TYPE } from '@/router/router-const';

export const userRouter = [
  {
    path: '/user',
    component: () => import('@/views/dashboard/dashboard'),
    children: [
      {
        path: '/user/change-password',
        name: 'UserChangePassword',
        meta: {
          title: '修改密码',
          permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
        },
        component: () =>
          import('views/user/change-password')
      }
    ]
  }
];

