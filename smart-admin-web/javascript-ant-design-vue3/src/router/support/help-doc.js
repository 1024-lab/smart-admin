/*
 * 帮助文档
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:53:19
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import HelpDocLayout from '/@/layout/help-doc-layout.vue';

export const helpDocRouters = [
  {
    path: '/help-doc',
    name: 'HelpDoc',
    component: HelpDocLayout,
    meta: {
      title: '帮助文档',
      hideInMenu: true,
    },
    children: [
      {
        path: '/help-doc/detail',
        component: () => import('/@/views/support/help-doc/user-view/help-doc-user-view.vue'),
      },
    ],
  },
];
