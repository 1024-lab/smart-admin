import { ROUTER_PERMISSION_TYPE } from '@/router/router-const';

export const school = [
  {
    path: '/school/detail',
    name: 'SchoolDetail',
    meta: {
      title: '学校详情',
      permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
    },
    component: () => import('@/views/crm/school/school-detail')
  },
  {
    path: '/school/track/add',
    name: 'SchoolTrackAdd',
    meta: {
      title: '学校拜访记录添加',
      permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
    },
    component: () => import('@/views/crm/school/add-track')
  },
  {
    path: '/school/track/detail',
    name: 'SchoolTrackDetail',
    meta: {
      title: '学校拜访记录详情',
      permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
    },
    component: () => import('@/views/crm/school/track-detail')
  },
  {
    path: '/school/update',
    name: 'SchoolUpdate',
    meta: {
      title: '更新学校',
      permissionType: ROUTER_PERMISSION_TYPE.NEED_LOGIN.value
    },
    component: () => import('@/views/crm/school/update-school')
  }
];

