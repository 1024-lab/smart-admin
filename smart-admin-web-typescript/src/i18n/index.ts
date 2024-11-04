/*
 * 国际化入口文件
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:01:19
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */

import en_US from './lang/en-US/index';
import zh_CN from './lang/zh-CN/index';
import { createI18n } from 'vue-i18n';
import { getInitializedLanguage } from '/@/store/modules/system/app-config';

// 语言选择数组
export const i18nList = [
  {
    text: '简体中文',
    value: 'zh_CN',
  },
  {
    text: 'English',
    value: 'en_US',
  },
];

export const messages = {
  zh_CN: zh_CN,
  en_US: en_US,
};

const i18n = createI18n({
  fallbackLocale: 'zh_CN', //预设语言环境
  globalInjection: true,
  legacy: false, //
  locale: getInitializedLanguage(), //默认初始化的语言
  messages, //
});

export default i18n;
