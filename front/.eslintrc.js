module.exports = {
  root: true,
  "extends": [
    "plugin:vue/essential",
    "@vue/standard"
  ],
  rules: {
    "generator-star-spacing": "off", //生成器函数*的前后空格
    // allow debugger during development
    "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off",
    "vue/no-parsing-error": [2, {
      "x-invalid-end-tag": false
    }],
    "no-const-assign": 2, //禁止修改const声明的变量
    "no-unused-vars": [0, { //禁止声明变量后却不使用
      // 允许声明未使用变量
      "vars": "all",
      // 参数不检查
      "args": "none"
    }],
    "quotes": [2, "single"], //单引号
    "singleQuote": true,
    "indent": 2, //缩进量
    "no-var": 2, //禁用var，用let和const代替
    "camelcase": 2, //强制驼峰法命名
    "eqeqeq": 1, //要求使用 === 和 !== 代替 == 和 != 操作符
    "no-eq-null": 2, //禁止对null使用==或!=运算符
    "no-sequences": 0, //禁用逗号操作符
    "semi": [2, "always"] //强制分号
  },
  parserOptions: {
    parser: "babel-eslint"
  }
}
