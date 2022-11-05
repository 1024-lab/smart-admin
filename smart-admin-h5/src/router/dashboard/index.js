import { ROUTER_PERMISSION_TYPE } from '@/router/router-const';

/**
 * dashboard首页
 */
export const dashboardRouter = [
  {
    path: '/',
    // redirect: '/dashboard/contact-company',
    redirect: '/dashboard/user',
    meta: {
      title: '首页',
      keepAlive: true,
      permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
    },
    component: () => import('@/views/dashboard/dashboard'),
    children: [
      // {
      //   path: '/dashboard/contact-company',
      //   name: 'ContactCompany',
      //   meta: {
      //     title: '往来单位',
      //     keepAlive: true,
      //     showTabbar: true,
      //     permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
      //   },
      //   component: () => import('@/views/erp/contact-company/contact-company')
      // },
      {
        path: '/dashboard/user',
        name: 'Mine',
        meta: {
          title: '我的',
          keepAlive: false,
          showTabbar: true,
          permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
        },
        component: () => import('@/views/user/index')
      }
    ]
  }
];
