import Main from '@/components/main';
// 三级路由演示
export const threeRouter = [
  {
    path: '/three-router',
    name: 'ThreeRouter',
    component: Main,
    meta: {
      title: '三级路由',
      icon: 'icon iconfont iconzujian'
    },
    children: [
      // 最大支持到三级菜单
      {
        path: '/three-router/level-two',
        name: 'LevelTwo',
        meta: {
          title: '三级菜单'
        },
        children: [
          {
            path: '/three-router/level-two/level-three1',
            name: 'ThreeLevelRouterView',
            meta: {
              title: '三级菜单子颗粒',
              privilege: [
                { title: '添加', name: 'three-level-router-view-add' },
                { title: '查询', name: 'three-level-router-view-query' },
              ]
            },
            component: () => import('@/views/business/three-level-router/three-level-router-view.vue')
          },
          {
            path: '/three-router/level-two/level-three2',
            name: 'RoleTwoTwo',
            meta: {
              title: '三级菜单子哈',
              privilege: [
                { title: '添加', name: 'roleTwoTwo-add' },
                { title: '删除', name: 'roleTwoTwo-delete' },
                { title: '删除', name: 'roleTwoTwo-delete1' },
                { title: '删除', name: 'roleTwoTwo-delete2' },
              ]
            },
            component: () => import('@/views/support/monitor/sql.vue')
          }
        ]
      },
      {
        path: '/three-router/level-two2',
        name: 'RoleOneOne',
        meta: {
          title: '二级菜单',
          privilege: [
            { title: '添加', name: 'roleOneOne-add' },
            { title: '删除', name: 'roleOneOne-delete' }
          ]
        },
        component: () => import('@/views/support/monitor/sql.vue')
      }
    ]
  }
];
