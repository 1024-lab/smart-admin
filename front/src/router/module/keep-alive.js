import Main from '@/components/main';
// 接口文档
export const keepAlive = [
  {
    path: '/keep-alive',
    name: 'KeepAlive',
    component: Main,
    meta: {
      title: 'KeepAlive',
      icon: 'icon iconfont iconxitongshezhi'
    },
    children: [
      {
        path: '/keep-alive/content-list',
        name: 'KeepAliveContentList',
        meta: {
          title: 'KeepAlive列表'
        },
        component: () => import('@/views/keep-alive/content-list.vue')
      },
      {
        path: '/keep-alive/add-content',
        name: 'KeepAliveAddContent',
        meta: {
          title: 'KeepAlive表单'
        },
        component: () => import('@/views/keep-alive/add-content.vue')
      }
    ]
  }
];
