/*
 * loading
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:54:50
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { defineStore } from 'pinia';
import { smartSentry } from '/@/lib/smart-sentry.js';

export const useSpinStore = defineStore({
  id: 'spin',
  state: () => ({
    loading: false,
  }),

  actions: {
    hide() {
      this.loading = false;
      // 安全的DOM操作，避免null引用错误
      try {
        const spins = document.querySelector('.ant-spin-nested-loading');
        if (spins) {
          spins.style.zIndex = '999';
        }
      } catch (error) {
        smartSentry.captureError('Spin hide操作失败:', error);
      }
    },
    show() {
      this.loading = true;
      // 安全的DOM操作，避免null引用错误
      try {
        const spins = document.querySelector('.ant-spin-nested-loading');
        if (spins) {
          spins.style.zIndex = '1001';
        }
      } catch (error) {
        smartSentry.captureError('Spin hide操作失败:', error);
      }
    },
  },
});
