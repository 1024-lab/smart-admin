import Main from '@/components/main';
// 基础设置
export const emailSetting = [
  {
    path: '/email',
    name: 'Email',
    component: Main,
    meta: {
      title: '邮件管理',
      icon: 'icon iconfont iconyoujianguanli'
    },
    children: [
      //  发送email
      {
        path: '/email/email-list',
        name: 'EmailList',
        meta: {
          title: '邮件管理',
          privilege: [
            { title: '查询', name: 'email-query' },
            { title: '新增', name: 'email-add' },
            { title: '编辑', name: 'email-update' },
            { title: '删除', name: 'email-delete' }
          ]
        },
        component: () => import('@/views/business/email/email-list.vue')
      },
      //  发送email
      {
        path: '/email/send-mail',
        name: 'SendMail',
        meta: {
          title: '发送邮件',
          privilege: [{ title: '发送', name: 'email-send' }]
        },
        component: () => import('@/views/business/email/send-mail.vue')
      }
    ]
  }
];
