
import Main from '@/components/main';


import { employee } from './employee';
import { file } from './file';
import { userLog } from './user-log';
import { systemSetting } from './system-setting';

// 业务
export const system = [
  {
    path: '/system',
    name: 'System',
    component: Main,
    meta: {
      title: '系统设置',
      topMenu: true,
      icon: 'icon iconfont iconxitongshezhi'
    },
    children: [
      ...employee,
      ...file,
      ...userLog,
      ...systemSetting
    ]
  }
];
