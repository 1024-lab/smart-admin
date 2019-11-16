import Main from '@/components/main';
// 基础设置
export const systemSetting = [
  {
    path: '/system-setting',
    name: 'SystemSetting',
    component: Main,
    meta: {
      title: '系统设置',
      icon: 'icon iconfont iconxitongshezhi'
    },

    children: [
      {
        path: '/system-setting/system-config',
        name: 'SystemConfig',
        meta: {
          title: '系统参数',
          childrenPoints: [
            {
              title: '查询系统参数',
              name: 'system-params-search'
            },
            {
              title: '添加系统参数',
              name: 'system-params-add'
            },
            {
              title: '修改系统参数',
              name: 'system-config-update'
            },
            {
              title: '搜索系统参数',
              name: 'system-config-search'
            }
          ]
        },
        component: () =>
          import('@/views/system-setting/system-config/system-config.vue')
      },
      {
        path: '/system-setting/system-privilege',
        name: 'SystemPrivilege',
        meta: {
          title: '菜单管理',
          childrenPoints: [
            {
              title: '编辑',
              name: 'privilege-main-update'
            },
            {
              title: '查询',
              name: 'privilege-main-search'
            }
          ]
        },
        component: () =>
          import('@/views/system-setting/system-privilege/system-privilege.vue')
      }
    ]
  }
];
