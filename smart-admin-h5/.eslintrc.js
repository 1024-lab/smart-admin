module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: [
    'plugin:vue/essential',
    'eslint:recommended'
  ],
  parserOptions: {
    parser: 'babel-eslint'
  },
  rules: {
    // 具体请看 https://github.com/vuejs/eslint-plugin-vue/blob/master/docs/rules/max-attributes-per-line.md
    'vue/max-attributes-per-line': [
      2,
      {
        singleline: 10,
        multiline: {
          max: 1,
          allowFirstLine: false
        }
      }
    ],
    // 在单行元素的内容前后需要换行符
    'vue/singleline-html-element-content-newline': 'off',
    // 在多行元素的内容之前和之后需要换行符
    'vue/multiline-html-element-content-newline': 'off',
    // JS/JSX中的组件名应该  大写驼峰 命名法
    'vue/name-property-casing': ['error', 'PascalCase'],
    // 给v-for设置键值，与key结合使用，可以高效的更新虚拟DOM
    "vue/require-v-for-key": 1,
    'vue/no-v-html': 'off',
    // 在对象中强制使用getter/setter
    'accessor-pairs': 2,
    // 在箭头函数之前/之后需要空格
    'arrow-spacing': [
      2,
      {
        before: true,
        after: true
      }
    ],
    // 在打开块之后和关闭块之前，禁止或强制执行块内部的空格
    'block-spacing': [2, 'always'],
    // 需要大括号样式
    'brace-style': [
      2,
      '1tbs',
      {
        allowSingleLine: true
      }
    ],
    // 需要驼峰命名
    camelcase: [
      0,
      {
        properties: 'always'
      }
    ],
    // 要求或禁止使用尾随逗号；最后一个属性是不需要逗号
    'comma-dangle': [2, 'never'],
    // 强制逗号旁边的间距： 左右一个空格
    'comma-spacing': [
      2,
      {
        before: false,
        after: true
      }
    ],
    // 逗号风格
    'comma-style': [2, 'last'],
    // 构建方法中使用super方法
    'constructor-super': 2,
    curly: [2, 'multi-line'],
    // 在dot之前和之后强制换行
    'dot-location': [2, 'property'],
    // 在文件末尾要求或禁止换行
    'eol-last': 2,
    // 是否使用全等
    eqeqeq: ['error', 'always', { null: 'ignore' }],
    // 在生成器函数中强制执行*周围的间距
    'generator-star-spacing': [2, {
      'before': true,
      'after': true
    }],
    // 强制执行回调错误处理
    'handle-callback-err': [1, '^(err|error)$'],
    // 强制执行一致的缩进
    'indent': [2, 2, {
      'SwitchCase': 1
    }],
    // 强制在JSX文件中一致使用单引号
    'jsx-quotes': [2, 'prefer-single'],
    // 在对象属性中强制键和值之间的一致间距
    'key-spacing': [2, {
      'beforeColon': false,
      'afterColon': true
    }],
    // 关键字前后强制执行一致的间距
    'keyword-spacing': [2, {
      'before': true,
      'after': true
    }],
    // 要求构造函数名称以大写字母开头
    'new-cap': [2, {
      'newIsCap': true,
      'capIsNew': false
    }],
    'new-parens': 2,     // 调用不带参数的函数时需要括号
    'no-array-constructor': 2,   // 禁止阵列构建器
    'no-caller': 2,      // 禁止使用来电者/被叫者
    'no-console': 'off',    // 不允许使用控制台
    'no-class-assign': 2,    // 禁止修改类声明的变量
    'no-cond-assign': 2,    // 在条件语句中禁止赋值运算符
    'no-const-assign': 2,    // 禁止修改使用const声明的变量
    'no-control-regex': 0,    // 禁止正则表达式中的控制字符
    'no-delete-var': 2,     // 禁止删除变量
    'no-dupe-args': 2,     // 在函数定义中禁止重复参数
    'no-dupe-class-members': 2,   // 禁止在类成员中重复名称
    'no-dupe-keys': 2,     // 禁止对象重复声明属性
    'no-duplicate-case': 2,    // 规则禁止重复案例标签
    'no-empty-character-class': 2,  // 禁止在正则表达式中使用空字符类
    'no-empty-pattern': 2,    // 不允许空的解构模式
    'no-eval': 2,      // 禁止使用eval（）
    'no-ex-assign': 2,     // 禁止在catch子句中重新分配异常
    'no-extend-native': 2,    // 禁止扩展原生对象
    'no-extra-bind': 2,     // 禁止不必要的功能绑定
    'no-extra-boolean-cast': 2,   // 禁止不必要的布尔类型转换
    'no-extra-parens': [2, 'functions'], // 禁止不必要的括号
    'no-fallthrough': 2,     // 禁止太多陈述描述
    'no-floating-decimal': 2,   // 禁止浮动小数
    'no-func-assign': 2,     // 禁止重新分配函数声明
    'no-implied-eval': 2,
    'no-inner-declarations': [2, 'functions'], // 禁止嵌套块中的变量或函数声明
    'no-invalid-regexp': 2,    // 禁止在RegExp中使用无效的正则表达式字符串
    'no-irregular-whitespace': 2,  // 不允许不规则的空白
    'no-iterator': 2,     // 禁止迭代器
    'no-label-var': 2,     // 禁止变量名称的标签
    'no-labels': [2, {
      'allowLoop': false,
      'allowSwitch': false
    }],
    'no-lone-blocks': 2,     // 禁止不必要的嵌套块
    'no-mixed-spaces-and-tabs': 2,  // 禁止使用混合空格和制表符进行缩进
    'no-multi-spaces': 2,    // 禁止多个空格
    'no-multi-str': 2,     // 禁止多行字符串
    'no-multiple-empty-lines': [2, {  // 禁止多个空行
      'max': 1
    }],
    'no-native-reassign': 2,
    'no-negated-in-lhs': 2,
    'no-new-object': 2,
    'no-new-require': 2,
    'no-new-symbol': 2,
    'no-new-wrappers': 2,
    'no-obj-calls': 2,
    'no-octal': 2,
    'no-octal-escape': 2,
    'no-path-concat': 2,
    'no-proto': 2,
    'no-redeclare': 2,
    'no-regex-spaces': 2,
    'no-return-assign': [2, 'except-parens'],
    'no-self-assign': 2,
    'no-self-compare': 2,
    'no-sequences': 2,
    'no-shadow-restricted-names': 2,
    'no-spaced-func': 2,
    'no-sparse-arrays': 2,
    'no-this-before-super': 2,
    'no-throw-literal': 2,
    'no-trailing-spaces': 2,
    'no-undef': 0,
    'no-undef-init': 2,
    'no-unexpected-multiline': 2,
    'no-unmodified-loop-condition': 2, // 禁止未修改的循环条件
    'no-unneeded-ternary': [2, {   // 当存在更简单的替代方案时，不允许三元运算符
      'defaultAssignment': false
    }],
    'no-unreachable': 2,     // 返回，抛出，继续和中断语句后禁止无法访问的代码
    'no-unsafe-finally': 2,    // 禁止finally块中的控制流语句
    'no-unused-vars': [2, {    // 禁止使用未声明的变量
      'vars': 'all',
      'args': 'none'
    }],
    'no-useless-call': 2,     // 禁止不必要的call()和apply()方法
    'no-useless-computed-key': 2,   // 禁止在对象上使用不必要的计算属性键
    'no-useless-constructor': 2,   // 禁止不必要的构造方法
    'no-useless-escape': 0,    // 禁止不必要的转义用法
    'no-whitespace-before-property': 2, // 在属性之前禁止空格
    'no-with': 2,
    'one-var': [2, {
      'initialized': 'never'
    }],
    'operator-linebreak': [2, 'after', { // 为维护强制执行一致的换行方式
      'overrides': {
        '?': 'before',
        ':': 'before'
      }
    }],
    'padded-blocks': [2, 'never'],   // 在块内要求或禁止填充
    'quotes': [2, 'single', {
      'avoidEscape': true,
      'allowTemplateLiterals': true
    }],
    semi: ["error", "always"],
    'space-before-blocks': [2, 'always'], // 不要存在多余的块空间
    'space-before-function-paren': [2, 'never'],
    'space-in-parens': [2, 'never'],
    'space-infix-ops': 2,
    'space-unary-ops': [2, {
      'words': true,
      'nonwords': false
    }],
    'spaced-comment': [2, 'always', {
      'markers': ['global', 'globals', 'eslint', 'eslint-disable', '*package', '!', ',']
    }],
    'template-curly-spacing': [2, 'never'],
    'use-isnan': 2,
    'valid-typeof': 2,
    'wrap-iife': [2, 'any'],
    'yield-star-spacing': [2, 'both'],
    'yoda': [2, 'never'],
    'prefer-const': 1,
    'no-debugger': process.env.NODE_ENV === 'production' ? 2 : 0,
    'object-curly-spacing': [2, 'always', {
      objectsInObjects: false
    }],
    'array-bracket-spacing': [2, 'never']
  }
}
