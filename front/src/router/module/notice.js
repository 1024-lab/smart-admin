import Main from '@/components/main';
// 消息管理
export const notice = [
  {
    path: '/notice',
    name: 'Notice',
    component: Main,
    meta: {
      title: '消息管理',
      icon: 'icon iconfont iconnews'
    },
    children: [
      {
        path: '/notice/notice-list',
        name: 'NoticeList',
        meta: {
          title: '通知管理',
          childrenPoints: [
            { title: '查询', name: 'notice-query' },
            { title: '添加', name: 'notice-add' },
            { title: '修改', name: 'notice-edit' },
            { title: '删除', name: 'notice-delete' },
            { title: '详情', name: 'notice-detail' },
            { title: '发送', name: 'notice-send' }
          ]
        },
        component: () => import('@/views/notice/notice-list.vue')
      },
      {
        path: '/notice/person-notice',
        name: 'PersonNotice',
        meta: {
          title: '个人消息',
          childrenPoints: [
            { title: '查询', name: 'person-notice-query' },
            { title: '详情', name: 'person-notice-detail' }
          ]
        },
        component: () => import('@/views/notice/person-notice.vue')
      }
    ]
  }
];
