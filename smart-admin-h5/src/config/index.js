console.log('project api url : ', process.env.VUE_APP_URL);

const isProductionEnv = ['production'].includes(process.env.NODE_ENV);

module.exports = {
  // 配置显示在浏览器标签的title
  title: 'Smart-Admin-H5',
  // token在Cookie中存储的天数，默认7天
  cookieExpires: 7,
  /**
   * @description api请求基础路径
   */
  baseUrl: {
    apiUrl: process.env.VUE_APP_URL,
    erpApiUrl: process.env.VUE_APP_ERP_URL,
    webSocketUrl: process.env.VUE_APP_SOCKET_URL
  },
  /**
   * 打包后静态资源地址;如果是走cdn的话，可以配置如下：
   * publicPath: isProd ? 'https://cdn.1024lab.net/static/smart-h5/' : '/'
   */
  publicPath: isProductionEnv ? '/manage-h5/' : '/',

  // ==================== cdn 相关 begin ====================
  cdn: {
    cdnResource: {
      css: [],
      js: [
        'https://cdn.bootcss.com/vue/2.6.11/vue.min.js',
        'https://cdn.bootcss.com/vue-router/3.2.0/vue-router.min.js',
        'https://cdn.bootcdn.net/ajax/libs/vuex/3.5.1/vuex.min.js',
        'https://cdn.bootcss.com/axios/0.19.2/axios.min.js',
        'https://cdn.bootcdn.net/ajax/libs/lodash.js/4.17.20/lodash.min.js'
      ]
    },
    // 指定资源加载cdn
    externals: {
      vue: 'Vue',
      'vue-router': 'VueRouter',
      vuex: 'Vuex',
      axios: 'axios',
      lodash: '_'
    }
  }
  // ==================== cdn 相关 end ====================
};
