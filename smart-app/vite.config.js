import { resolve } from 'path';
import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'

const pathResolve = (dir) => {
  return resolve(__dirname, '.', dir);
};

export default defineConfig({
  transpileDependencies:['@dcloudio/uni-ui'],
  plugins: [
    uni(),
  ],
  root: process.cwd(),
  resolve: {
    alias: [
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
  // 发布时删除console
  build: {
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true,
      },
    },
  },
})
