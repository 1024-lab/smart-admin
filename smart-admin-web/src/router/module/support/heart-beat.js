import Main from '@/components/main';
// 心跳服务
export const heartBeat = [
  {
    path: '/heart-beat',
    name: 'HeartBeat',
    component: Main,
    meta: {
      title: '心跳服务',
      icon: 'icon iconfont icondingshirenwu'
    },

    children: [
      {
        path: '/heart-beat/heart-beat-list',
        name: 'HeartBeatList',
        meta: {
          title: '心跳服务',
          icon: 'icon iconfont icondingshirenwu',
          privilege: [
            {
              title: '查询任务',
              name: 'heart-beat-query'
            }
          ]
        },
        component: () => import('@/views/support/heart-beat/heart-beat-list.vue')
      }
    ]
  }
];
