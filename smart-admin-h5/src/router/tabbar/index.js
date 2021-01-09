import { ROUTER_PERMISSION_TYPE } from '../router-const';

/**
 * 主要用于 tabbar 的路由
 */
export const tabbarRouter = [
  {
    path: '/',
    redirect: '/contact-company',
    meta: {
      title: '首页',
      keepAlive: true,
      permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
    },
    component: () => import('views/main/tabbar-main'),
    children: [
      {
        path: '/home',
        name: 'Home',
        meta: {
          title: '首页',
          keepAlive: true,
          permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
        },
        component: () => import('@/views/home/index')
      },
      {
        path: '/business',
        name: 'Business',
        meta: {
          title: '业务中心',
          keepAlive: false,
          permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
        },
        component: () => import('@/views/business/index')
      },
      {
        path: '/mine',
        name: 'Mine',
        meta: {
          title: '关于我',
          keepAlive: false,
          permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
        },
        component: () => import('@/views/mine/index')
      }
    ]
  }
];
