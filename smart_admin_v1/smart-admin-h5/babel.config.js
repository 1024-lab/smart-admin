
module.exports = {
  presets: [['@vue/cli-plugin-babel/preset', {useBuiltIns: 'usage', corejs: 3}]],
  plugins:[
    // vant-ui 按需引入，详情：https://github.com/ElementUI/babel-plugin-component
    // [
    //   'import',
    //   {
    //     libraryName: 'vant',
    //     libraryDirectory: 'es',
    //     style: true
    //   },
    //   'vant'
    // ]
  ]
};
