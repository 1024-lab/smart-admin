import Main from '@/components/main';
// 系统监控
export const monitor = [
  {
    path: '/monitor',
    component: Main,
    name: 'Monitor',
    meta: {
      title: '系统监控',
      icon: 'icon iconfont iconxitongjiankong'
    },
    children: [
      // 在线人数
      {
        path: '/monitor/online-user',
        name: 'OnlineUser',
        meta: {
          title: '在线人数',
          privilege: [{ title: '查询', name: 'online-user-search' }]
        },
        component: () => import('@/views/support/monitor/online-user.vue')
      },
      // SQL监控
      {
        path: '/monitor/sql',
        name: 'Sql',
        meta: {
          title: 'SQL监控'
        },
        component: () => import('@/views/support/monitor/sql.vue')
      }
    ]
  }
];
