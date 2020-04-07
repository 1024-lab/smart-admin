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
            name: 'RoleOneTwo',
            meta: {
              title: '三级A',
              privilege: [
                { title: '添加', name: 'roleOneTwo-add' },
                { title: '删除', name: 'roleOneTwo-delete' }
              ]
            },
            component: () => import('@/views/system/system-setting/system-config/system-config.vue')
          },
          {
            path: '/three-router/level-two/level-three2',
            name: 'RoleTwoTwo',
            meta: {
              title: '三级B',
              privilege: [
                { title: '添加', name: 'roleTwoTwo-add' },
                { title: '删除', name: 'roleTwoTwo-delete' }
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
