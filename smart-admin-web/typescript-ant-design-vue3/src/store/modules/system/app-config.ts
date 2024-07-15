/*
 * 项目的配置信息
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:53:47
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { defineStore } from 'pinia';
import { appDefaultConfig } from '/@/config/app-config';
import localStorageKeyConst from '/@/constants/local-storage-key-const';
import { smartSentry } from '/@/lib/smart-sentry';
import { AppConfig } from '/@/types/config';
import { localRead } from '/@/utils/local-util';

let state: AppConfig = { ...appDefaultConfig };

let appConfigStr = localRead(localStorageKeyConst.APP_CONFIG);
let language = appDefaultConfig.language;
if (appConfigStr) {
  try {
    state = JSON.parse(appConfigStr);
    language = state.language;
  } catch (e) {
    smartSentry.captureError(e);
  }
}

/**
 * 获取初始化的语言
 */
export const getInitializedLanguage = function () {
  return language;
};

export const useAppConfigStore = defineStore({
  id: 'appConfig',
  state: (): AppConfig => ({
    // 读取config下的默认配置
    ...state,
  }),
  actions: {
    reset(): void {
      let key: keyof AppConfig;
      for (key in appDefaultConfig) {
        this.$state[key] = appDefaultConfig[key];
      }
    },
    showHelpDoc(): void {
      this.helpDocFlag = true;
    },
    hideHelpDoc(): void {
      this.helpDocFlag = false;
    },
  },
});
