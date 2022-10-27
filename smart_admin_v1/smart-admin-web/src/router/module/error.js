// 错误页
export const error = [
  {
    path: '/401',
    name: 'Error401',
    meta: {
      hideInMenu: true,
      access: true,
      noValidatePrivilege: true
    },
    component: () => import('@/views/error-page/401.vue')
  },
  {
    path: '/500',
    name: 'Error500',
    meta: {
      hideInMenu: true,
      access: true,
      noValidatePrivilege: true
    },
    component: () => import('@/views/error-page/500.vue')
  },
  {
    path: '*',
    name: 'http://localhost:8080/#employee/role-employee-manage',
    meta: {
      hideInMenu: true,
      access: true,
      noValidatePrivilege: true
    },
    component: () => import('@/views/error-page/404.vue')
  }
];
