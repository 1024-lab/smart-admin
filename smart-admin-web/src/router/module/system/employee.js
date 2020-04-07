import Main from '@/components/main';
// 基础设置
export const employee = [
  {
    path: '/employee',
    component: Main,
    name: 'Employee',
    meta: {
      title: '人员管理',
      icon: 'icon iconfont iconrenyuanguanli'
    },
    children: [
      //  角色管理页面路由
      {
        path: '/employee/role',
        name: 'RoleManage',
        meta: {
          title: '角色管理',
          privilege: [
            {
              title: '添加角色',
              name: 'add-role'
            },
            {
              title: '删除角色',
              name: 'delete-role'
            },
            {
              title: '编辑角色',
              name: 'update-role'
            },
            {
              title: '修改角色权限',
              name: 'update-role-privilege'
            },
            {
              title: '添加成员',
              name: 'add-employee-role'
            },
            {
              title: '移除成员',
              name: 'delete-employee-role'
            },
            {
              title: '批量移除',
              name: 'delete-employee-role-batch'
            },
            {
              title: '查询成员',
              name: 'search-employee-list'
            },
            {
              title: '查询数据范围',
              name: 'query-data-scope'
            },
            {
              title: '更新数据范围',
              name: 'update-data-scope'
            }
          ]
        },
        component: () => import('@/views/system/employee/role/role-manage.vue')
      },
      // 岗位管理页面路由 新
      {
        path: '/employee/position',
        name: 'PositionList',
        meta: {
          title: '岗位管理',
          privilege: [
            {
              title: '查询',
              name: 'search-position'
            },
            {
              title: '添加',
              name: 'add-position'
            },
            {
              title: '修改',
              name: 'update-position'
            },
            {
              title: '删除',
              name: 'delete-position'
            }
          ]
        },
        component: () => import('@/views/system/employee/position/position-list.vue')
      },
      // 人员管理页面路由
      {
        path: '/employee/role-employee-manage',
        name: 'RoleEmployeeManage',
        meta: {
          title: '员工管理',
          privilege: [
            {
              title: '添加部门',
              name: 'add-department'
            },
            {
              title: '编辑部门',
              name: 'update-department'
            },
            {
              title: '删除部门',
              name: 'delete-department'
            },
            {
              title: '查询',
              name: 'search-department'
            },
            {
              title: '添加成员',
              name: 'add-employee'
            },
            {
              title: '编辑成员',
              name: 'update-employee'
            },
            {
              title: '禁用',
              name: 'disabled-employee'
            },
            {
              title: '批量操作',
              name: 'disabled-employee-batch'
            },
            {
              title: '员工角色编辑',
              name: 'update-employee-role'
            },
            {
              title: '删除员工',
              name: 'delete-employee'
            },
            {
              title: '重置密码',
              name: 'reset-employee-password'
            }
          ]
        },
        component: () =>
          import('@/views/system/employee/role-employee/role-employee-manage.vue')
      }
    ]
  }
];
