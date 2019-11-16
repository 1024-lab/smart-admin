// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import router from './router';
import store from './store';
import iView from 'iview';
import i18n from '@/locale';
import config from '@/config';
import importDirective from '@/directives';
import JsonViewer from 'vue-json-viewer';
import _ from 'lodash';
// import { directive as clickOutside } from 'v-click-outside-x';
import * as vClickOutside from 'v-click-outside-x';
import installPlugin from '@/plugins';
import './themes/index.less';
import '@/assets/icons/iconfont.css';
import 'slick-carousel/slick/slick.css';
import { Decimal } from 'decimal.js';
// 枚举管理
import Enum from 'vue-enum';
import enumInfo from '@/constants';
// 处理table操作按钮
import tableAction from './lib/table-action';
Vue.prototype.$tableAction = tableAction;
Vue.use(Enum, { enumInfo });
Vue.use(iView, {
  i18n: (key, value) => i18n.t(key, value)
});
Vue.use(JsonViewer);
Vue.use(vClickOutside);
Number.prototype.toFixed = function (length) {
  let x = new Decimal(this);
  return x.toFixed(length);
};

/**
 * @description 注册admin内置插件
 */
installPlugin(Vue);
/**
 * @description 生产环境关掉提示
 */
Vue.config.productionTip = false;
/**
 * @description 全局注册应用配置
 */
Vue.prototype.$config = config;
/**
 * 注册指令
 */
importDirective(Vue);
// Vue.directive('clickOutside', clickOutside);

window._ = _;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  i18n,
  store,
  render: h => h(App)
});
