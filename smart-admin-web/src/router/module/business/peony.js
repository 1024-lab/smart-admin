import Main from '@/components/main';
// t_peony路由
export const peony = [
    {
        path: '/peony',
        name: 'Peony',
        component: Main,
        meta: {
            title: '牡丹管理',
            icon: 'icon iconfont iconyoujianguanli'
        },
        children: [
            //  牡丹花 列表
            {
                path: '/peony/peony-list',
                name: 'PeonyList',
                meta: {
                    title: '牡丹花列表',
                    privilege: [
                        { title: '查询', name: 'peony-list-query' },
                        { title: '新增', name: 'peony-list-add' },
                        { title: '编辑', name: 'peony-list-update' },
                        { title: '批量删除', name: 'peony-list-batch-delete' },
                        { title: '批量导出', name: 'peony-list-batch-export' },
                        { title: '导出全部', name: 'peony-list-export-all' }
                    ]
                },
                component: () => import('@/views/business/peony/peony-list.vue')
            },
            //  牡丹花 列表 1
            {
                path: '/peony/peony-list1',
                name: 'PeonyList1',
                meta: {
                    title: '牡丹花列表1',
                    privilege: [
                        { title: '查询', name: 'peony1-list-query' },
                        { title: '新增', name: 'peony1-list-add' },
                        { title: '编辑', name: 'peony1-list-update' },
                        { title: '批量删除', name: 'peony1-list-batch-delete' },
                        { title: '批量导出', name: 'peony1-list-batch-export' },
                        { title: '导出全部', name: 'peony1-list-export-all' }
                    ]
                },
                component: () => import('@/views/business/peony/peony-list.vue')
            }
        ]
    }
];
