/*
 * 代码格式化配置
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-12 14:44:18
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
module.exports = {
  printWidth: 150, // 每行代码长度（默认80）
  tabWidth: 2, // 缩进空格数
  useTabs: false, //不用tab缩进
  semi: true, //// 在语句末尾打印分号
  singleQuote: true, // 使用单引号而不是双引号
  vueIndentScriptAndStyle: true, //Vue文件脚本和样式标签缩进
  quoteProps: 'as-needed', // 更改引用对象属性的时间 可选值"<as-needed|consistent|preserve>"
  jsxSingleQuote: true, // 在JSX中使用单引号而不是双引号
  trailingComma: 'es5', //多行时尽可能打印尾随逗号。（例如，单行数组永远不会出现逗号结尾。） 可选值"<none|es5|all>"，默认none
  bracketSpacing: true, // 在对象文字中的括号之间打印空格
  jsxBracketSameLine: false, //jsx 标签的反尖括号需要换行
  arrowParens: 'always', // 在单独的箭头函数参数周围包括括号 always：(x) => x \ avoid：x => x
  rangeStart: 0, // 这两个选项可用于格式化以给定字符偏移量（分别包括和不包括）开始和结束的代码
  rangeEnd: Infinity,
  requirePragma: false, // 指定要使用的解析器，不需要写文件开头的 @prettier
  insertPragma: false, // 不需要自动在文件开头插入 @prettier
  proseWrap: 'preserve', // 使用默认的折行标准 always\never\preserve
  htmlWhitespaceSensitivity: 'css', // 指定HTML文件的全局空格敏感度 css\strict\ignore
  endOfLine: 'auto', // 因为prettier的规范和eslint的换行规则不同，所以这个必须配置。要不然每次打开文件都会有一堆的警告;换行符使用 lf 结尾是 可选值"<auto|lf|crlf|cr
};
