import Main from '@/components/main';
// 接口文档
export const apiDoc = [
  {
    path: '/api-doc',
    component: Main,
    name: 'ApiDoc',
    meta: {
      title: '接口文档',
      icon: 'icon iconfont iconjiekouwendang'
    },

    children: [
      {
        path: '/api-doc/swagger',
        name: 'Swagger',
        meta: {
          title: 'Swagger接口文档',
          icon: 'icon iconfont iconjiekouwendang'
        },
        component: () => import('@/views/api-doc/swagger.vue')
      }
    ]
  }
];
