// vue 三大核心
import Vue from 'vue';
import router from '@/router';
import store from '@/store';

// 更好的兼容性
import 'core-js/stable';
import 'regenerator-runtime/runtime';
// 移动端适配
import 'lib-flexible/flexible.js';
import 'vant/lib/index.css';
// 引入首个组件
import App from './App.vue';
// 引入自定义主题样式
import './themes/index.scss';
// 引入过滤器
import './filters';
// 引入配置信息
import config from '@/config';
// 引入自定义smart 插件
import SmartPlugin from './plugins/smart';
// Import component
import Loading from 'vue-loading-overlay';
// Import stylesheet
import 'vue-loading-overlay/dist/vue-loading.css';

import Vant from 'vant';
import 'vant/lib/index.css';

// sentry错误预警
import SmartSentry from './plugins/smart-sentry';

import Enum from 'vue-enum';

import enumInfo from '@/constants';

import _ from 'lodash';

Vue.use(Vant);

Vue.use(Loading);

Vue.use(SmartPlugin);

Vue.use(SmartSentry);

Vue.prototype.$config = config;

Vue.config.productionTip = false;

Vue.use(Enum, {
  enumInfo
});

window._ = _;

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
});
