/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-07-05
 * @LastEditors: zhuoda
 */
module.exports = {
  root: true, //此项是用来告诉eslint找当前配置文件不能往父级查找
  env: {
    browser: true,
    es2021: true,
    node: true,
  },
  parser: 'vue-eslint-parser', //使用vue-eslint-parser 来解析vue文件中的 template和script
  parserOptions: {
    ecmaVersion: 12, // 默认情况下，ESLint使用的是ECMAScript5语法，此处我们设置的选项是 es12
    sourceType: 'module', // 指定js导入的方式
  },
  extends: ['plugin:vue/vue3-essential', 'eslint:recommended', 'plugin:vue/base'],
  globals: {
    defineProps: 'readonly',
    defineEmits: 'readonly',
    defineExpose: 'readonly',
    withDefaults: 'readonly',
  },
  plugins: ['vue'],
  rules: {
    'no-unused-vars': [
      'error',
      // we are only using this rule to check for unused arguments since TS
      // catches unused variables but not args.
      { varsIgnorePattern: '.*', args: 'none' },
    ],
    'space-before-function-paren': 'off',

    'vue/attributes-order': 'off',
    'vue/one-component-per-file': 'off',
    'vue/html-closing-bracket-newline': 'off',
    'vue/max-attributes-per-line': 'off',
    'vue/multiline-html-element-content-newline': 'off',
    'vue/singleline-html-element-content-newline': 'off',
    'vue/attribute-hyphenation': 'off',
    'vue/require-default-prop': 'off',
    'vue/multi-word-component-names': [
      'error',
      {
        ignores: ['index'], //需要忽略的组件名
      },
    ],
    'vue/html-self-closing': [
      'error',
      {
        html: {
          void: 'always',
          normal: 'never',
          component: 'always',
        },
        svg: 'always',
        math: 'always',
      },
    ],
    // Enable vue/script-setup-uses-vars rule
    'vue/script-setup-uses-vars': 'error',
  },
};
