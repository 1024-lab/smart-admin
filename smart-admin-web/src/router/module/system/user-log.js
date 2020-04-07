import Main from '@/components/main';
// 用户日志
export const userLog = [
  {
    path: '/user-log',
    name: 'UserLog',
    component: Main,
    meta: {
      title: '用户日志',
      icon: 'ios-paper-outline'
    },
    children: [
      //  发送email
      {
        path: '/user-log/user-operate-log',
        name: 'UserOperateLog',
        meta: {
          title: '用户操作日志',
          privilege: [
            { title: '查询', name: 'user-operate-log-search' },
            { title: '详情', name: 'user-operate-log-detail' },
            { title: '删除', name: 'user-operate-log-delete' }
          ]
        },
        component: () => import('@/views/system/user-log/user-operate-log.vue')
      },
      // 人员管理页面路由
      {
        path: '/user-log/user-login-log',
        name: 'UserLoginLog',
        meta: {
          title: '用户登录日志',
          privilege: [
            { title: '查询', name: 'user-login-log-search' },
            { title: '删除', name: 'user-login-log-delete' }
          ]
        },
        component: () => import('@/views/system/user-log/user-login-log.vue')
      }
    ]
  }
];
