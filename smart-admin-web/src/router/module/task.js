import Main from '@/components/main';
// 任务调度
export const task = [
  {
    path: '/task',
    name: 'Task',
    component: Main,
    meta: {
      title: '定时任务',
      icon: 'icon iconfont icondingshirenwu'
    },

    children: [
      {
        path: '/system-setting/task-list',
        name: 'TaskList',
        meta: {
          title: '任务管理',
          icon: 'icon iconfont icondingshirenwu',
          childrenPoints: [
            {
              title: '查询任务',
              name: 'task-search'
            },
            {
              title: '刷新任务',
              name: 'task-refresh'
            },
            {
              title: '添加任务',
              name: 'task-add'
            },
            {
              title: '编辑任务',
              name: 'task-update'
            },
            {
              title: '暂停任务',
              name: 'task-pause'
            },
            {
              title: '恢复任务',
              name: 'task-resume'
            },
            {
              title: '立即运行任务',
              name: 'task-run'
            },
            {
              title: '查看任务日志',
              name: 'task-query-log'
            },
            {
              title: '删除任务',
              name: 'task-delete'
            }
          ]
        },
        component: () => import('@/views/task/task-list.vue')
      }
    ]
  }
];
