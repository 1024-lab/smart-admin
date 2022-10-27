import Main from '@/components/main';
// 首页
export const home = [
  {
    path: '/',
    name: '_home',
    redirect: '/home',
    component: Main,
    meta: {
      title: '首页',
      noKeepAlive: true,
      hideInMenu: true,
      noValidatePrivilege: true,
      icon: 'icon iconfont iconxitongshezhi'
    },
    children: [
      {
        path: '/home',
        name: 'Home',
        meta: {
          title: '首页',
          noValidatePrivilege: true,
          noKeepAlive: true
        },
        component: () => import('@/views/home')
      }
    ]
  }
];
