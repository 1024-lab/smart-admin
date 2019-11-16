import Main from '@/components/main';
// 动态加载
export const reload = [
  {
    path: '/reload',
    name: 'Reload',
    component: Main,
    meta: {
      title: '动态加载',
      icon: 'icon iconfont icondongtaijiazai'
    },

    children: [
      {
        path: '/reload/smart-reload-list',
        name: 'SmartReloadList',
        meta: {
          title: 'SmartReload',
          icon: 'icon iconfont icondongtaijiazai',
          childrenPoints: [
            {
              title: '查询',
              name: 'smart-reload-search'
            },
            {
              title: '执行reload',
              name: 'smart-reload-update'
            },
            {
              title: '查看执行结果',
              name: 'smart-reload-result'
            }
          ]
        },
        component: () =>
          import('@/views/reload/smart-reload/smart-reload-list.vue')
      }
    ]
  }
];
