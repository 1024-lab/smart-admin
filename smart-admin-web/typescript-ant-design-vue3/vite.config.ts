/*
 * vite配置
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-05-02 23:44:56
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），2012-2022
 */
import { resolve } from 'path';
import vue from '@vitejs/plugin-vue';

const pathResolve = (dir) => {
  return resolve(__dirname, '.', dir);
};
export default {
  base: process.env.NODE_ENV === 'production' ? '/manages/' : '/',
  root: process.cwd(),
  resolve: {
    alias: [
      // 国际化替换
      {
        find: 'vue-i18n',
        replacement: 'vue-i18n/dist/vue-i18n.cjs.js',
      },
      // 绝对路径重命名：/@/xxxx => src/xxxx
      {
        find: /\/@\//,
        replacement: pathResolve('src') + '/',
      },
      {
        find: /^~/,
        replacement: '',
      },
    ],
  },
  // 服务端渲染
  server: {
    host: '0.0.0.0',
    port: 8081,
  },
  plugins: [vue()],
  optimizeDeps: {
    include: ['ant-design-vue/es/locale/zh_CN', 'dayjs/locale/zh-cn', 'ant-design-vue/es/locale/en_US'],
    exclude: ['vue-demi'],
  },
  build: {
    brotliSize: false,
    chunkSizeWarningLimit: 2000,
  },
  css: {
    preprocessorOptions: {
      less: {
        modifyVars: {
          hack: `true; @import (reference) "${resolve('src/theme/index.less')}";`,
        },
        javascriptEnabled: true,
      },
    },
  },
  define: {
    __INTLIFY_PROD_DEVTOOLS__: false,
    'process.env': process.env,
  },
};
