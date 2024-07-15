/*
 * 枚举、图标、工具类
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:59:23
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { SmartEnumPlugin } from '/@/types/smart-enum';
import * as lodash from 'lodash';

declare module '*.vue' {
  import { Component } from 'vue';
  const component: Component;
  export default component;
}

// 对vue进行类型补充说明
declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    // 常量插件
    $smartEnumPlugin: SmartEnumPlugin;
    // 常量图标
    $antIcons: Object;
    // lodash工具类
    $lodash: lodash;
  }
}
