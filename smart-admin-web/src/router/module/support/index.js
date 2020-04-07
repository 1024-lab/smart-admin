
import Main from '@/components/main';


import { apiDoc } from './api-doc';
import { heartBeat } from './heart-beat';
import { monitor } from './monitor';
import { reload } from './reload';
import { task } from './task';

// 业务
export const support = [
  {
    path: '/support',
    name: 'Support',
    component: Main,
    meta: {
      title: '开发专用',
      topMenu: true,
      icon: 'icon iconfont iconjiekouwendang'
    },
    children: [
      ...apiDoc,
      ...heartBeat,
      ...monitor,
      ...reload,
      ...task
    ]
  }
];
