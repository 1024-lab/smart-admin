import Main from '@/components/main';
// 文件服务
export const file = [
  {
    path: '/file',
    name: 'File',
    component: Main,
    meta: {
      title: '文件服务',
      icon: 'ios-cloud-upload'
    },
    children: [
      {
        path: '/file/file-list',
        name: 'FileList',
        meta: {
          title: '文件列表',
          icon: 'ios-cloud-upload',
          privilege: [
            { title: '查询', name: 'file-filePage-query' },
            { title: '上传', name: 'file-filePage-upload' },
            { title: '下载', name: 'file-filePage-download' }
          ]
        },
        component: () => import('@/views/system/file/file-list.vue')
      }
    ]
  }
];
