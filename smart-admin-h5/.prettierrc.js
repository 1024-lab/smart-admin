module.exports = {
  // 缩进字节数
  tabWidth: 2,
  // 使用单引号代替双引号
  singleQuote: true,
  // 在对象或数组最后一个元素后面是否加逗号（在ES5中加尾逗号）
  trailingComma: 'none',
  // 句尾添加分号
  semi: true,
  // 默认值。因为使用了一些折行敏感型的渲染器（如GitHub comment）而按照markdown文本样式进行折行
  proseWrap: 'always',
  //  (x) => {} 箭头函数参数只有一个时是否要有小括号。avoid：省略括号
  arrowParens: 'avoid',
  // 在对象，数组括号与文字之间加空格 "{ foo: bar }"
  bracketSpacing: true,
  // 在jsx中把'>' 是否单独放一行
  jsxBracketSameLine: false,
  // 缩进不使用tab，使用空格
  useTabs: false,
  // 不让prettier使用eslint的代码格式进行校验
  eslintIntegration: true,
  overrides: [
    {
      files: '.prettierrc',
      options: {
        parser: 'json'
      }
    }
  ],
  endOfLine: 'auto'
}
